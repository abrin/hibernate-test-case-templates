package org.tdar.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersistableUtils {

    /**
     * Should only be invoked after performing basic checks within your equals(Object) method to ensure that it's not null and equivalent types.
     * 
     * Returns true if
     * <ol>
     * <li>the two ids for both persistables are equal OR
     * <li>each element in the class specific List returned by getEqualityFields() are equal
     * </ol>
     * 
     * NOTE: if the two ids for both persistables are equal, the rest of the equality fields *should* also be equal, otherwise
     * we will run into problems with hashCode().
     * 
     * Returns false otherwise.
     * 
     * @param <T>
     * @param a
     * @param b
     * @return
     */
    public static boolean isEqual(Persistable a, Persistable b) {
        // null is never equal to anything
        if (a == null) {
            LoggerFactory.getLogger(Persistable.class).trace("false b/c 'a' is null");
            return false;
        }
        Logger logger = LoggerFactory.getLogger(a.getClass());
        if (b == null) {
            logger.trace("false b/c 'b' is null");
            return false;
        }

        // objects that are the same are equal
        if (a == b) {
            logger.trace("object equality");
            return true;
        }

        /*
         * Some tests are failing b/c javaasist subclass? or bytecode manipulation of tDAR classes:
         * eg: AdvancedSearchControllerITCase.testResourceCreatorPerson:
         * result: final equality false b/c of class class org.tdar.core.bean.resource.Document != class
         * org.tdar.core.bean.resource.Document_$$_javassist_62
         */
        if (!(a.getClass().isAssignableFrom(b.getClass()) || b.getClass().isAssignableFrom(a.getClass()))) {
            logger.trace("false b/c of class {} != {} ", a.getClass(), b.getClass());
            return false;
        }

        EqualsBuilder equalsBuilder = new EqualsBuilder();

        if (a.getEqualityFields().isEmpty()) {
            if (isTransient(a) || isTransient(b)) {
                return false;
            } else {
                equalsBuilder.append(a.getId(), b.getId());
            }
        } else {
            Object[] selfEqualityFields = a.getEqualityFields().toArray();
            Object[] candidateEqualityFields = b.getEqualityFields().toArray();
            logger.trace("comparing equality fields {} != {} ", selfEqualityFields, candidateEqualityFields);
            equalsBuilder.append(selfEqualityFields, candidateEqualityFields);
        }

        return equalsBuilder.isEquals();
    }

    public static <P extends Persistable> Map<Long, P> createIdMap(Collection<P> items) {
        Map<Long, P> map = new HashMap<>();
        for (P item : items) {
            if (item == null) {
                continue;
            }
            map.put(item.getId(), item);
        }
        return map;
    }

    public static int toHashCode(Persistable persistable) {
        // since we typically get called from instance method it's unlikely persistable will be null, but lets play safe...
        if (persistable == null) {
            return 0;
        }
        HashCodeBuilder builder = new HashCodeBuilder(23, 37);

        if (CollectionUtils.isEmpty(persistable.getEqualityFields())) {
            if (isTransient(persistable)) {
                return System.identityHashCode(persistable);
            } else {
                builder.append(persistable.getId());
            }
        } else {
            builder.append(persistable.getEqualityFields().toArray());
        }

        return builder.toHashCode();
    }

//    /*
//     * Adds the contents of the collection to the set, and removes anything was not in the incoming collections (intersection) + add all
//     * 
//     * @return boolean representing whether the exiting set was changed in any way
//     */
//    public static <C> boolean reconcileSet(Set<C> existing, Collection<C> incoming) {
//        if (existing == null) {
//            throw new TdarRuntimeException("persistable.collection_null");
//        }
//        if (incoming == null) {
//            if (!CollectionUtils.isEmpty(existing)) {
//                existing.clear();
//                return true;
//            }
//            return false;
//        }
//
//        boolean changedRetain = existing.retainAll(incoming);
//        boolean changedAddAll = existing.addAll(incoming);
//
//        return (changedRetain || changedAddAll);
//    }

    public static boolean isNotTransient(Persistable persistable) {
        return !isTransient(persistable);
    }

    public static boolean isNotNullOrTransient(Persistable persistable) {
        return !isNullOrTransient(persistable);
    }

    public static boolean isNotNullOrTransient(Number persistable) {
        return !isNullOrTransient(persistable);
    }

    public static boolean isTransient(Persistable persistable) {
        // object==primative only works for certain primative values (see http://stackoverflow.com/a/3815760/103814)
        return (persistable == null) || isNullOrTransient(persistable.getId());
    }

    public static boolean isNullOrTransient(Persistable persistable) {
        return (persistable == null) || isTransient(persistable);
    }

    public static boolean isNullOrTransient(Number val) {
        return (val == null) || (val.longValue() == -1L);
    }

    public static <T extends Persistable> List<Long> extractIds(Collection<T> persistables) {
        List<Long> ids = new ArrayList<>();
        for (T persistable : persistables) {
            if (persistable != null) {
                ids.add(persistable.getId());
            } else {
                ids.add(null);
            }
        }
        return ids;
    }

    public static <T extends Persistable> List<Long> extractIds(Collection<T> persistables, int max) {
        List<Long> ids = new ArrayList<>();
        int count = 0;
        for (T persistable : persistables) {
            if (persistable != null) {
                ids.add(persistable.getId());
            } else {
                ids.add(null);
            }
            count++;
            if (count == max) {
                break;
            }
        }
        return ids;
    }

}
