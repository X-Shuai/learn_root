package com.xs.linkList;

/**
 * @program: learn_root
 * @description: 反转队列
 * @author: xs-shuai.com
 * @create: 2020-09-14 22:22
 **/
public class ReverseLinked {

    /***
     * 反转一个单链表。
     *
     * 示例:
     *
     * 输入: 1->2->3->4->5->NULL
     * 输出: 5->4->3->2->1->NULL
     * @param head
     * @return
     */
    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            //拿到下一个
            ListNode nextTemp = curr.next;
            curr.next = prev;//反向
            prev = curr;
            curr = nextTemp;
        }
        return prev;

    }
}
//class ListNode {
//    int val;
//    ListNode next;
//    ListNode(int x) { val = x; }
//}
