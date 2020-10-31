package com.xs.linkList;

/**
 * @program: learn_root
 * @description: 两两交换链表中的节点
 * @author: xs-shuai.com
 * @create: 2020-09-14 22:38
 **/
public class swapNodesInPairs {
    public ListNode swapPairs(ListNode head) {
            if (head==null||head.next==null){
                return  head;
            }
        ListNode next = head.next;
        head.next = swapPairs(next.next);
        next.next = head;
        return next;

    }
}

class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}



