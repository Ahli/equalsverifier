package nl.jqno.equalsverifier.internal.reflection;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import nl.jqno.equalsverifier.testhelpers.types.Point;
import org.junit.Test;

public class InPlaceObjectAccessorTest {
    @Test
    public void of() {
        Point p = new Point(1, 2);
        ObjectAccessor<Point> actual = ObjectAccessor.of(p);
        assertTrue(actual instanceof InPlaceObjectAccessor);
    }

    @Test
    public void get() {
        Object foo = new Object();
        InPlaceObjectAccessor<Object> accessor = create(foo);
        assertSame(foo, accessor.get());
    }

    @SuppressWarnings("unchecked")
    private <T> InPlaceObjectAccessor<T> create(T object) {
        return new InPlaceObjectAccessor<T>(object, (Class<T>) object.getClass());
    }
}
