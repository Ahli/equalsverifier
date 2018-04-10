package nl.jqno.equalsverifier.internal.prefabvalues.factories;

import nl.jqno.equalsverifier.internal.prefabvalues.PrefabValues;
import nl.jqno.equalsverifier.internal.prefabvalues.Tuple;
import nl.jqno.equalsverifier.internal.prefabvalues.TypeTag;
import nl.jqno.equalsverifier.internal.reflection.ConditionalInstantiator;

import java.util.LinkedHashSet;

import static nl.jqno.equalsverifier.internal.reflection.Util.classes;
import static nl.jqno.equalsverifier.internal.reflection.Util.objects;

/**
 * Implementation of {@link PrefabValueFactory} that reflectively instantiates
 * collections by copying them from a collection that was already instantiated.
 */
public final class ReflectiveCollectionCopyFactory<T> extends AbstractReflectiveGenericFactory<T> {
    private final String typeName;
    private final Class<?> declaredParameterRawType;
    private final Class<?> actualParameterRawType;
    private final String factoryType;
    private final String factoryMethod;

    public ReflectiveCollectionCopyFactory(String typeName, Class<?> parameterRawType, String factoryType, String factoryMethod) {
        this(typeName, parameterRawType, parameterRawType, factoryType, factoryMethod);
    }

    public ReflectiveCollectionCopyFactory(String typeName, Class<?> declaredParameterRawType, Class<?> actualParameterRawType,
            String factoryType, String factoryMethod) {
        this.typeName = typeName;
        this.declaredParameterRawType = declaredParameterRawType;
        this.actualParameterRawType = actualParameterRawType;
        this.factoryType = factoryType;
        this.factoryMethod = factoryMethod;
    }

    @Override
    public Tuple<T> createValues(TypeTag tag, PrefabValues prefabValues, LinkedHashSet<TypeTag> typeStack) {
        LinkedHashSet<TypeTag> clone = cloneWith(typeStack, tag);
        TypeTag singleParameterTag = copyGenericTypesInto(actualParameterRawType, tag);
        prefabValues.realizeCacheFor(singleParameterTag, clone);

        ConditionalInstantiator ci = new ConditionalInstantiator(typeName);
        Object red = ci.callFactory(factoryType, factoryMethod,
                classes(declaredParameterRawType), objects(prefabValues.giveRed(singleParameterTag)));
        Object black = ci.callFactory(factoryType, factoryMethod,
                classes(declaredParameterRawType), objects(prefabValues.giveBlack(singleParameterTag)));
        Object redCopy = ci.callFactory(factoryType, factoryMethod,
            classes(declaredParameterRawType), objects(prefabValues.giveRed(singleParameterTag)));

        return Tuple.of(red, black, redCopy);
    }
}
