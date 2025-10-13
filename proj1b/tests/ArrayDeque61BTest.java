import deque.ArrayDeque61B;

import deque.Deque61B;
import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

/** All tests ensure cycling of {@code nextFirst} and {@code nextLast} indices on the underlying array.
 *  we also want to make sure that resizing are tested.
 */
public class ArrayDeque61BTest {

    @Test
    @DisplayName("ArrayDeque61B has no fields besides backing array and primitives")
    void noNonTrivialFields() {
        List<Field> badFields = Reflection.getFields(ArrayDeque61B.class)
                .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(Object[].class) || f.isSynthetic()))
                .toList();

        assertWithMessage("Found fields that are not array or primitives").that(badFields).isEmpty();
    }

    @Test
    public void addFirstTestBasic() {
        Deque61B<Integer> lld1 = new ArrayDeque61B<>();
        for (int i = 0; i < 10; i++) {
            lld1.addFirst(i);
        }
        assertThat(lld1.toList()).containsExactly(9, 8, 7, 6, 5, 4, 3, 2, 1, 0).inOrder();
    }

    @Test
    public void addLastTestBasic() {
        Deque61B<Integer> lld1 = new ArrayDeque61B<>();
        for (int i = 0; i < 10; i++) {
            lld1.addLast(i);
        }
        assertThat(lld1.toList()).containsExactly(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).inOrder();
    }

    @Test
    /** This test performs interspersed addFirst and addLast calls. */
    public void addFirstAndAddLastTest() {
        Deque61B<Integer> lld1 = new ArrayDeque61B<>();

        lld1.addLast(0);
        lld1.addLast(1);
        lld1.addFirst(-1);
        lld1.addLast(2);
        lld1.addFirst(-2);
        lld1.addLast(3);
        lld1.addLast(4);
        lld1.addLast(5);
        lld1.addFirst(-3);
        lld1.addLast(6);

        assertThat(lld1.toList()).containsExactly(-3, -2, -1, 0, 1, 2, 3, 4, 5, 6).inOrder();
    }

    @Test
    public void getTest() {
        Deque61B<Integer> lld1 = new ArrayDeque61B<>();
        assertThat(lld1.get(0)).isNull();

        for (int i = 0; i < 6; i++) {
            lld1.addFirst(i);
        }
        lld1.removeLast();
        lld1.removeLast();
        lld1.removeLast();
        lld1.addLast(12);

        assertThat(lld1.toList()).containsExactly(5, 4, 3, 12).inOrder();

        assertThat(lld1.get(-1)).isNull();
        assertThat(lld1.get(0)).isEqualTo(5);
        assertThat(lld1.get(3)).isEqualTo(12);
        assertThat(lld1.get(8)).isNull();
    }

    @Test
    public void toListEmptyTest() {
        Deque61B<String> lld1 = new ArrayDeque61B<>();
        assertThat(lld1.toList()).isEmpty();
    }

    @Test
    public void removeFirstTest() {
        Deque61B<Integer> lld1 = new ArrayDeque61B<>();
        assertThat(lld1.removeFirst()).isEqualTo(null);

        for (int i = 0; i < 16; i++) {
            lld1.addLast(i);
        }

        for (int i = 0; i < 16; i++) {
            assertThat(lld1.removeFirst()).isEqualTo(i);
        }
        assertThat(lld1.removeFirst()).isEqualTo(null);
    }

    @Test
    public void removeLastTest() {
        Deque61B<Integer> lld1 = new ArrayDeque61B<>();
        assertThat(lld1.removeLast()).isEqualTo(null);

        for (int i = 0; i < 16; i++) {
            lld1.addFirst(i);
        }

        for (int i = 0; i < 16; i++) {
            assertThat(lld1.removeLast()).isEqualTo(i);
        }
        assertThat(lld1.removeLast()).isEqualTo(null);
    }

    @Test
    public void addAfterRemoveTest() {
        Deque61B<Integer> lld1 = new ArrayDeque61B<>();
        for (int i = 0; i < 16; i++) {
            lld1.addLast(i);
        }
        for (int i = 0; i < 16; i++) {
            lld1.removeFirst();
        }
        assertThat(lld1.toList()).isEmpty();

        lld1.addFirst(1234);
        assertWithMessage("add first after remove to empty").that(lld1.toList()).containsExactly(1234);

        Deque61B<Integer> lld2 = new ArrayDeque61B<>();
        for (int i = 0; i < 16; i++) {
            lld2.addFirst(i);
        }
        for (int i = 0; i < 16; i++) {
            lld2.removeLast();
        }
        assertThat(lld2.toList()).isEmpty();

        lld2.addLast(1234);
        assertWithMessage("add last after remove to empty").that(lld2.toList()).containsExactly(1234);
    }

    @Test
    public void sizeAndIsEmptyTest() {
        Deque61B<Integer> lld1 = new ArrayDeque61B<>();
        assertThat(lld1.isEmpty()).isTrue();
        assertThat(lld1.size()).isEqualTo(0);

        lld1.addLast(0);
        assertThat(lld1.isEmpty()).isFalse();
        assertThat(lld1.size()).isEqualTo(1);

        lld1.addFirst(1);
        assertThat(lld1.isEmpty()).isFalse();
        assertThat(lld1.size()).isEqualTo(2);
    }

    @Test
    public void sizeWithAddAndRemoveTest() {
        Deque61B<Integer> lld1 = new ArrayDeque61B<>();
        assertThat(lld1.size()).isEqualTo(0);

        for (int i = 0; i < 8; i++) {
            if (i % 3 == 0) {
                lld1.addFirst(i);
            } else {
                lld1.addLast(i);
            }
            assertThat(lld1.size()).isEqualTo(i + 1);
        }

        for (int i = 0; i < 8; i++) {
            if (i % 4 == 0) {
                lld1.removeLast();
            } else {
                lld1.removeFirst();
            }
            assertThat(lld1.size()).isEqualTo(8 - 1 - i);
        }

        lld1.removeFirst();
        assertThat(lld1.size()).isEqualTo(0);

        lld1.removeLast();
        assertThat(lld1.size()).isEqualTo(0);
    }
}
