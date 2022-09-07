package RecursionLearning;

public class PartialReverse {

    ListNode successor = null;

    public ListNode partialReverse(ListNode head, int n) {

        if (head == null) {
            successor = head.next;
            return head;
        }

        if (n == 1) {
            return head;
        }

        ListNode last = this.partialReverse(head.next, n - 1);
        head.next.next = head;

        head.next = successor;

        return last;

    }

}
