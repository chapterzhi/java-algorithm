package com.test

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class AVLTree {

    private AVLTreeNode root = null;

    public AVLTree() {
    }

    public void rr(AVLTreeNode node) {
        AVLTreeNode parent = node.getParent();
        AVLTreeNode leftChild = node.getLeft();
        if (Objects.isNull(parent)) {
            root = leftChild;
        } else {
            if (parent.getLeft() == node) {
                parent.setLeft(leftChild);
            } else {
                parent.setRight(leftChild);
            }
        }
        leftChild.setParent(parent);
        node.setLeft(leftChild.getRight());
        if (Objects.nonNull(node.getLeft())) {
            node.getLeft().setParent(node);
        }
        leftChild.setRight(node);
        node.setParent(leftChild);
        setHeight(node);
        setHeight(node.getParent());
    }

    public void ll(AVLTreeNode node) {
        AVLTreeNode parent = node.getParent();
        AVLTreeNode rightChild = node.getRight();
        if (node == root) {
            root = rightChild;
        } else {
            if (parent.getLeft() == node) {
                parent.setLeft(rightChild);
            } else {
                parent.setRight(rightChild);
            }
        }
        rightChild.setParent(parent);
        node.setRight(rightChild.getLeft());
        if (Objects.nonNull(node.getLeft())) {
            node.getLeft().setParent(node);
        }
        rightChild.setLeft(node);
        node.setParent(rightChild);
        setHeight(node);
        setHeight(node.getParent());
    }

    public void setHeight(AVLTreeNode cur) {
        cur.setHeight(Math.max(cur.getLeft() == null ? 0 : cur.getLeft().getHeight(),
                cur.getRight() == null ? 0 : cur.getRight().getHeight()) + 1);
    }

    public boolean notBalance(AVLTreeNode cur) {
        if ((cur.getLeft() == null && cur.getRight() != null && cur.getRight().getHeight() >= 2) ||
                (cur.getRight() == null && cur.getLeft() != null && cur.getLeft().getHeight() >= 2) ||
                (cur.getLeft() != null && cur.getRight() != null
                        && Math.abs(cur.getLeft().getHeight() - cur.getRight().getHeight()) > 1)) {
            return true;
        }
        return false;
    }

    /**
     * < 0: 左边高  >0 : 右边高
     * @param cur
     * @return
     */
    public Integer whichHigher(AVLTreeNode cur) {
        int leftHeight = cur.getLeft() == null ? 0 : cur.getLeft().getHeight();
        int rightHeight = cur.getRight() == null ? 0 : cur.getRight().getHeight();
        return rightHeight - leftHeight;
    }

    public void insert(int val) {
        AVLTreeNode insertNode = new AVLTreeNode(val, 1);
        if (root == null) {
            root = insertNode;
            return;
        }

        AVLTreeNode cur = root;
        while (cur != null) {
            if (cur.getData() == val) {
                return;
            }
            if (cur.getData() > val) {
                if (cur.getLeft() == null) {
                    cur.setLeft(insertNode);
                    insertNode.setParent(cur);
                    break;
                }
                cur = cur.getLeft();
            } else {
                if (cur.getRight() == null) {
                    cur.setRight(insertNode);
                    insertNode.setParent(cur);
                    break;
                }
                cur = cur.getRight();
            }
        }

        cur = insertNode;
        while (cur.getParent() != null) {
            cur = cur.getParent();
            setHeight(cur);
        }

        reset(insertNode);

    }

    /**
     * 重整树
     * @param cur
     */
    public void reset (AVLTreeNode cur) {
        while (cur != null) {
            setHeight(cur);
            if (!notBalance(cur)) {
                cur = cur.getParent();
                continue;
            }
            if (whichHigher(cur) < 0) {
                if (whichHigher(cur.getLeft()) < 0) {
                    rr(cur);
                } else {
                    ll(cur.getLeft());
                    rr(cur);
                }
            } else {
                if (whichHigher(cur.getRight()) > 0) {
                    ll(cur);
                } else {
                    rr(cur.getRight());
                    ll(cur);
                }
            }
            cur = cur.getParent();
        }
    }

    public void printTree() {
        printTree(5);
    }

    public void printTree(Integer high) {
        Integer leaves = (int) Math.pow(2, high - 1);
        Queue<AVLTreeNode> queue = new LinkedList<>();
        queue.offer(root);
        queue.offer(new AVLTreeNode(Integer.MIN_VALUE));
        int level = 0;
        List<String> line = new ArrayList<>();
        while (!queue.isEmpty()) {
            if (queue.peek() == null) {
                line.add("     ");
                queue.offer(null);
                queue.offer(null);
                queue.poll();
            } else {
                if (queue.peek().getData() == Integer.MIN_VALUE) {
                    Integer paddingLen = (leaves * 6 - 5 * line.size()) / line.size();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < paddingLen; i++) {
                        sb.append(' ');
                    }
                    String padding = sb.toString();
                    for (int i = 0; i < line.size(); i++) {
                        if (i == 0) {
                            System.out.print(padding.substring(0, padding.length() / 2) + line.get(i));
                        } else {
                            System.out.print(padding + line.get(i));
                        }
                    }
                    System.out.println();
                    System.out.println();
                    line.clear();
                    level ++;
                    if (level >= high) {
                        return;
                    }
                    queue.remove();
                    queue.offer(new AVLTreeNode(Integer.MIN_VALUE));
                } else {
                    Integer c = queue.peek().getHeight();
//                    Integer c = queue.peek().getParent() == null ? 99 : queue.peek().getParent().getData();
                    line.add(String.format("%2d %2d", queue.peek().getData(), c));
                    queue.offer(queue.peek().getLeft());
                    queue.offer(queue.peek().getRight());
                    queue.poll();
                }
            }
        }
    }

    public void remove(Integer data) {
        AVLTreeNode cur = root;
        while (cur != null) {
            if (cur.getData() == data) {
                break;
            }
            if (cur.getData() > data) {
                cur = cur.getLeft();
            } else {
                cur = cur.getRight();
            }
        }
        if (cur == null) {
            return;
        }
        removeNode(cur);

    }

    public void removeNode(AVLTreeNode cur) {
        if (cur.getLeft() == null && cur.getRight() == null) {
            if (cur == root) {
                root = null;
                return;
            }
            if (cur == cur.getParent().getLeft()) {
                cur.getParent().setLeft(null);
            } else {
                cur.getParent().setRight(null);
            }
            reset(cur.getParent());
        } else if (cur.getLeft() == null) {
            if (cur == root) {
                root = cur.getRight();
            } else {
                if (cur == cur.getParent().getLeft()) {
                    cur.getParent().setLeft(cur.getRight());
                } else {
                    cur.getParent().setRight(cur.getRight());
                }
            }
            cur.getRight().setParent(cur.getParent());
            reset(cur.getParent());
        } else if (cur.getRight() == null) {
            if (cur == root) {
                root = cur.getLeft();
            } else {
                if (cur == cur.getParent().getLeft()) {
                    cur.getParent().setLeft(cur.getLeft());
                } else {
                    cur.getParent().setRight(cur.getLeft());
                }
            }
            cur.getLeft().setParent(cur.getParent());
            reset(cur.getParent());
        } else {
            AVLTreeNode replaceNode = cur.getRight();
            while (replaceNode.getLeft() != null) {
                replaceNode = replaceNode.getLeft();
            }
            if (replaceNode.getLeft() != null || replaceNode.getRight() != null) {
                cur.setData(replaceNode.getData());
                removeNode(replaceNode);
                return;
            }
            AVLTreeNode replaceParent = replaceNode.getParent();
            if (replaceNode == replaceNode.getParent().getLeft()) {
                replaceNode.getParent().setLeft(null);
            } else {
                replaceNode.getParent().setRight(null);
            }
            replaceNode.setLeft(cur.getLeft());
            replaceNode.getLeft().setParent(replaceNode);
            replaceNode.setRight(cur.getRight());
            replaceNode.getRight().setParent(replaceNode);
            replaceNode.setParent(cur.getParent());
            if (cur == root) {
                root = replaceNode;
            } else {
                if (cur == cur.getParent().getLeft()) {
                    cur.getParent().setLeft(replaceNode);
                } else {
                    cur.getParent().setRight(replaceNode);
                }
            }
            if (cur == replaceParent) {
                reset(replaceNode);
            } else {
                reset(replaceParent);
            }
        }
    }

    public static void main(String[] args) {
        AVLTree avlTree = new AVLTree();
        avlTree.insert(50);
        avlTree.insert(60);
        avlTree.insert(70);
        avlTree.insert(40);
        avlTree.insert(20);
        avlTree.insert(45);
        avlTree.insert(63);
        avlTree.insert(10);
        avlTree.insert(15);
        avlTree.insert(5);
        avlTree.printTree();
        avlTree.remove(70);
        avlTree.printTree();
        avlTree.remove(50);
        avlTree.printTree();
        avlTree.remove(45);
        avlTree.printTree();
        avlTree.remove(40);
        avlTree.printTree();

//        avlTree.insert(3);
//        avlTree.insert(4);
//        avlTree.printTree();
//        avlTree.remove(3);
//        avlTree.printTree();
//        avlTree.remove(4);
//        avlTree.printTree();

    }

}
