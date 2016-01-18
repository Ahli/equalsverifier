/*
 * Copyright 2014 Jan Ouwens
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.jqno.equalsverifier.integration.extended_contract;

import com.google.common.base.Optional;
import com.google.common.collect.*;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.joda.time.*;
import org.junit.Test;

import static nl.jqno.equalsverifier.testhelpers.Util.defaultEquals;
import static nl.jqno.equalsverifier.testhelpers.Util.defaultHashCode;

public class ExternalApiClassesTest {
    @Test
    public void succeed_whenClassUsesGoogleGuavaMultiset() {
        EqualsVerifier.forClass(GuavaMultisetContainer.class)
                .verify();
    }

    @Test
    public void succeed_whenClassUsesGoogleGuavaImmutableCollection() {
        EqualsVerifier.forClass(GuavaImmutableContainer.class)
                .verify();
    }

    @Test
    public void succeed_whenClassUsesOtherGoogleGuavaClass() {
        EqualsVerifier.forClass(GuavaOtherContainer.class)
                .verify();
    }

    @Test
    public void succeed_whenClassUsesJodaTimeClass() {
        EqualsVerifier.forClass(JodaTimeContainer.class)
                .verify();
    }

    @SuppressWarnings("unused") // because of the use of defaultEquals and defaultHashCode
    static final class GuavaMultisetContainer {
        private final Multiset<?> multiset;
        private final SortedMultiset<?> sortedMultiset;
        private final HashMultiset<?> hashMultiset;
        private final TreeMultiset<?> treeMultiset;
        private final LinkedHashMultiset<?> linkedHashMultiset;
        private final ConcurrentHashMultiset<?> concurrentHashMultiset;
        private final ImmutableMultiset<?> immutableMultiset;

        public GuavaMultisetContainer(Multiset<?> multiset, SortedMultiset<?> sortedMultiset, HashMultiset<?> hashMultiset,
                TreeMultiset<?> treeMultiset, LinkedHashMultiset<?> linkedHashMultiset,
                ConcurrentHashMultiset concurrentHashMultiset, ImmutableMultiset<?> immutableMultiset) {
            this.multiset = multiset; this.sortedMultiset = sortedMultiset; this.hashMultiset = hashMultiset;
            this.treeMultiset = treeMultiset; this.linkedHashMultiset = linkedHashMultiset;
            this.concurrentHashMultiset = concurrentHashMultiset; this.immutableMultiset = immutableMultiset;
        }

        @Override public boolean equals(Object obj) { return defaultEquals(this, obj); }
        @Override public int hashCode() { return defaultHashCode(this); }
    }

    @SuppressWarnings("unused") // because of the use of defaultEquals and defaultHashCode
    static final class GuavaImmutableContainer {
        private final ImmutableList<?> iList;
        private final ImmutableMap<?, ?> iMap;
        private final ImmutableSet<?> iSet;
        private final ImmutableSortedMap<?, ?> iSortedMap;
        private final ImmutableSortedSet<?> iSortedSet;

        // CHECKSTYLE: ignore ParameterNumber for 1 line.
        public GuavaImmutableContainer(ImmutableList<?> immutableList, ImmutableMap<?, ?> immutableMap, ImmutableSet<?> immutableSet,
                ImmutableSortedMap<?, ?> iSortedMap, ImmutableSortedSet<?> iSortedSet) {
            this.iList = immutableList; this.iMap = immutableMap; this.iSet = immutableSet; this.iSortedMap = iSortedMap;
            this.iSortedSet = iSortedSet;
        }

        @Override public boolean equals(Object obj) { return defaultEquals(this, obj); }
        @Override public int hashCode() { return defaultHashCode(this); }
    }

    @SuppressWarnings("unused") // because of the use of defaultEquals and defaultHashCode
    static final class GuavaOtherContainer {
        private final ImmutableSortedMultiset<?> iSortedMultiset;
        private final ImmutableListMultimap<?, ?> iListMultimap;
        private final ImmutableSetMultimap<?, ?> iSetMultimap;
        private final ImmutableBiMap<?, ?> iBiMap;
        private final ImmutableTable<?, ?, ?> iTable;
        private final Range<?> range;
        private final Optional<?> optional;

        // CHECKSTYLE: ignore ParameterNumber for 1 line.
        public GuavaOtherContainer(
                ImmutableSortedMultiset<?> iSortedMultiset, ImmutableListMultimap<?, ?> iListMultimap,
                ImmutableSetMultimap<?, ?> iSetMultimap, ImmutableBiMap<?, ?> immutableBiMap, ImmutableTable<?, ?, ?> iTable,
                Range<?> range, Optional<?> optional) {
            this.iSortedMultiset = iSortedMultiset;
            this.iListMultimap = iListMultimap; this.iSetMultimap = iSetMultimap; this.iBiMap = immutableBiMap;
            this.iTable = iTable; this.range = range; this.optional = optional;
        }

        @Override public boolean equals(Object obj) { return defaultEquals(this, obj); }
        @Override public int hashCode() { return defaultHashCode(this); }
    }

    @SuppressWarnings("unused") // because of the use of defaultEquals and defaultHashCode
    static final class JodaTimeContainer {
        private final LocalDate localDate;
        private final LocalTime localTime;
        private final LocalDateTime localDateTime;
        private final Chronology chronology;
        private final DateTimeZone dateTimeZone;
        private final Partial partial;
        private final PeriodType periodType;
        private final Period period;
        private final YearMonth yearMonth;
        private final MonthDay monthDay;

        // CHECKSTYLE: ignore ParameterNumber for 1 line.
        public JodaTimeContainer(LocalDate localDate, LocalTime localTime, LocalDateTime localDateTime, Chronology chronology,
                DateTimeZone dateTimeZone, Partial partial, PeriodType periodType, Period period, YearMonth yearMonth, MonthDay monthDay) {
            this.localDate = localDate; this.localTime = localTime; this.localDateTime = localDateTime; this.chronology = chronology;
            this.dateTimeZone = dateTimeZone; this.partial = partial; this.periodType = periodType; this.period = period;
            this.yearMonth = yearMonth; this.monthDay = monthDay;
        }

        @Override public boolean equals(Object obj) { return defaultEquals(this, obj); }
        @Override public int hashCode() { return defaultHashCode(this); }
    }
}
