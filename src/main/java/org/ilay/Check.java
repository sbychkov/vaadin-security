package org.ilay;

import org.ilay.api.Restrict;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

class Check {

    static <T extends Collection> T notNullOrEmpty(T collection) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException("collection must not be null or empty");
        }

        return collection;
    }

    static <T extends Map<U, V>, U, V> T notNullOrEmpty(T map) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException("map must not be null or empty");
        }

        return map;
    }

    static void setCurrentRestrict(Restrict restrict) {
        final Optional<CurrentRestrict> currentRestrictOptional = VaadinAbstraction.getFromSessionStore(CurrentRestrict.class);

        if (currentRestrictOptional.isPresent()) {
            final CurrentRestrict currentRestrict = currentRestrictOptional.get();

            currentRestrict.setRestrict(restrict);
        } else {
            CurrentRestrict currentRestrict = new CurrentRestrict();
            currentRestrict.setRestrict(restrict);
            VaadinAbstraction.storeInSession(CurrentRestrict.class, currentRestrict);
        }
    }

    static void currentRestrictIs(Restrict restrict) {
        requireNonNull(restrict);

        final Optional<CurrentRestrict> currentRestrictOptional = VaadinAbstraction.getFromSessionStore(CurrentRestrict.class);

        state(currentRestrictOptional.isPresent());

        final CurrentRestrict currentRestrict = currentRestrictOptional.get();

        state(currentRestrict.getRestrict() == restrict);
    }

    static void noUnclosedRestrict() {
        final Optional<CurrentRestrict> currentRestrictOptional = VaadinAbstraction.getFromSessionStore(CurrentRestrict.class);

        if (!currentRestrictOptional.isPresent()) {
            return;
        }

        @SuppressWarnings("OptionalGetWithoutIsPresent")
        CurrentRestrict currentRestrict = currentRestrictOptional.get();

        if (currentRestrict.getRestrict() != null) {

            final Restrict restrict = currentRestrict.getRestrict();

            if (restrict instanceof ComponentRestrict) {
                throw new IllegalStateException("Authorization.bindComponent() or Authorization.bindComponents() has been called without calling to() on the returned Restrict-object");
            } else if (restrict instanceof ViewRestrict) {
                throw new IllegalStateException("Authorization.bindView() or Authorization.bindViews() has been called without calling to() on the returned Restrict-object");
            } else {
                //will never come here
                throw new IllegalStateException("unknown Restrict");
            }
        }
    }

    static <T> T[] arraySanity(T[] array) {
        requireNonNull(array, "array must not be null");
        arg(array.length > 0, "array must not be empty");

        for (int i = 0; i < array.length; i++) {
            T t = array[i];
            requireNonNull(t, "elements in array must not be null");

            for (int i1 = 0; i1 < array.length; i1++) {
                if (i == i1) {
                    continue;
                }

                T t2 = array[i1];

                arg(!t2.equals(t), "");
            }
        }

        return array;
    }

    static String notNullOrEmpty(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("input String must not be null or empty");
        }

        return input;
    }

    static void arg(boolean condition, String message, Object... parameters) {
        if (!condition) {
            throw new IllegalArgumentException(format(message, parameters));
        }
    }

    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "OptionalGetWithoutIsPresent"})
    static <T> T present(Optional<T> optional) {
        requireNonNull(optional);

        state(optional.isPresent());

        return optional.get();
    }

    static void state(boolean condition) {
        if (!condition) {
            throw new IllegalStateException();
        }
    }

    static void state(boolean condition, String message, Object... parameters) {
        if (!condition) {
            throw new IllegalStateException(format(message, parameters));
        }
    }

    private static class CurrentRestrict {
        private Restrict currentRestrict;

        Restrict getRestrict() {
            return currentRestrict;
        }

        void setRestrict(Restrict currentRestrict) {
            this.currentRestrict = currentRestrict;
        }
    }
}
