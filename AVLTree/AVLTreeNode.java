package com.test;

import lombok.Data;

@Data
public class AVLTreeNode {

    private AVLTreeNode left;
    private AVLTreeNode right;
    private AVLTreeNode parent;
    private Integer height;
    private Integer data;

    public AVLTreeNode(Integer data) {
        this.data = data;
    }

    public AVLTreeNode(Integer data, Integer height) {
        this.data = data;
        this.height = height;
    }
}
