package RecursionLearning;

public class AddTwoNumII {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode reversedL1 = this.reverse(l1);
        ListNode reversedL2 = this.reverse(l2);
        ListNode reversed = this.traverseRe(reversedL1, reversedL2);
        return this.reverse(reversed);
    }

    private ListNode reverse(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode reversed = this.reverse(head.next);
        head.next.next = head;
        head.next = null;
        return reversed;

    }

    public ListNode traverseRe(ListNode l1, ListNode l2) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;
        ListNode newNode = new ListNode();
        newNode.val = l1.val + l2.val;
        if (newNode.val < 10) {
            newNode.next = this.traverseRe(l1.next, l2.next);
        } else {
            newNode.val -= 10;
            newNode.next = this.traverseRe(this.traverseRe(l1.next, l2.next), new ListNode(1));
        }
        return newNode;

    }
}
