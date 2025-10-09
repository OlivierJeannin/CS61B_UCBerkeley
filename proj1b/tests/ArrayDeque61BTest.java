import deque.ArrayDeque61B;

import deque.Deque61B;
import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

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
    /** In this test, we have three different assert statements that verify that addFirst works correctly. */
    public void addFirstTestBasic() {
        Deque61B<Integer> lld1 = new ArrayDeque61B<>();
        for (int i = 0; i < 6; i++) {  // add 6 elements to ensure cycling
            lld1.addFirst(i);
        }
        assertThat(lld1.toList()).containsExactly(5, 4, 3, 2, 1, 0).inOrder();
    }

    @Test
    /** In this test, we use only one assertThat statement. IMO this test is just as good as addFirstTestBasic.
     *  In other words, the tedious work of adding the extra assertThat statements isn't worth it. */
    public void addLastTestBasic() {
        Deque61B<Integer> lld1 = new ArrayDeque61B<>();
        for (int i = 0; i < 6; i++) {
            lld1.addLast(i);
        }
        assertThat(lld1.toList()).containsExactly(0, 1, 2, 3, 4, 5).inOrder();
    }

    @Test
    /** This test performs interspersed addFirst and addLast calls. */
    public void addFirstAndAddLastTest() {
        Deque61B<Integer> lld1 = new ArrayDeque61B<>();

        lld1.addLast(0);
        lld1.addLast(1);
        lld1.addFirst(-1);
        lld1.addLast(2);
        lld1.addLast(3);
        lld1.addLast(4);
        lld1.addLast(5);
        lld1.addFirst(-2);

        assertThat(lld1.toList()).containsExactly(-2, -1, 0, 1, 2, 3, 4, 5).inOrder();
    }

    @Test
    public void getTest() {
        Deque61B<Integer> lld1 = new ArrayDeque61B<>();
        assertThat(lld1.get(1234)).isNull();

        int i;
        for (i = 0; i < 3; i++) {
            lld1.addFirst(i);
        }
        lld1.addLast(i);
        for (i = 4; i < 7; i++) {
            lld1.addFirst(i);
        }

        assertThat(lld1.get(-1)).isNull();
        assertThat(lld1.get(0)).isEqualTo(6);
        assertThat(lld1.get(5)).isEqualTo(0);
        assertThat(lld1.get(6)).isEqualTo(3);
        assertThat(lld1.get(8)).isNull();
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
    public void toListEmptyTest() {
        Deque61B<String> lld1 = new ArrayDeque61B<>();
        assertThat(lld1.toList()).isEmpty();
    }

    @Test
    public void removeFirstTest() {
        Deque61B<Integer> lld1 = new ArrayDeque61B<>();

        lld1.addLast(0);
        lld1.addFirst(-1);

        assertThat(lld1.removeFirst()).isEqualTo(-1);
        assertThat(lld1.toList()).containsExactly(0);

        assertThat(lld1.removeFirst()).isEqualTo(0);
        assertThat(lld1.toList()).isEmpty();

        assertThat(lld1.removeFirst()).isNull();
        assertThat(lld1.toList()).isEmpty();
    }

    @Test
    public void removeLastTest() {
        Deque61B<Integer> lld1 = new ArrayDeque61B<>();

        lld1.addLast(0);
        lld1.addFirst(-1);

        assertThat(lld1.removeLast()).isEqualTo(0);
        assertThat(lld1.toList()).containsExactly(-1);

        assertThat(lld1.removeFirst()).isEqualTo(-1);
        assertThat(lld1.toList()).isEmpty();

        assertThat(lld1.removeFirst()).isNull();
        assertThat(lld1.toList()).isEmpty();
    }

    @Test
    public void addAfterRemoveTest() {
        Deque61B<Integer> lld1 = new ArrayDeque61B<>();

        lld1.addFirst(1);
        lld1.addLast(2);
        lld1.removeFirst();
        lld1.removeLast();
        assertThat(lld1.toList()).isEmpty();

        lld1.addFirst(1234);
        assertWithMessage("add first after remove to empty").that(lld1.toList()).containsExactly(1234);

        // Note that currently lld1 is NOT empty
        lld1.addLast(3);
        lld1.removeFirst();
        lld1.removeLast();
        assertThat(lld1.toList()).isEmpty();

        lld1.addLast(1234);
        assertWithMessage("add last after remove to empty").that(lld1.toList()).containsExactly(1234);
    }

    @Test
    public void sizeWithAddAndRemoveTest() {
        Deque61B<Integer> lld1 = new ArrayDeque61B<>();
        assertThat(lld1.size()).isEqualTo(0);

        lld1.addLast(0);
        assertThat(lld1.size()).isEqualTo(1);

        lld1.addFirst(1);
        assertThat(lld1.size()).isEqualTo(2);

        lld1.removeLast();
        assertThat(lld1.size()).isEqualTo(1);

        lld1.removeFirst();
        assertThat(lld1.size()).isEqualTo(0);

        lld1.removeFirst();
        assertThat(lld1.size()).isEqualTo(0);
    }
}
