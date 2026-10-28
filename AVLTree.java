
import java.util.Scanner;

public class AVLTree {

    static class Node {
        int accountNo;
        int height;
        Node left, right;

        Node(int accountNo) {
            this.accountNo = accountNo;
            height = 1;
        }
    }

    static Node root = null;

    static int height(Node node) {
        return node == null ? 0 : node.height;
    }

    static int getBalance(Node node) {
        return node == null ? 0 :
               height(node.left) - height(node.right);
    }

    static int max(int a, int b) {
        return (a > b) ? a : b;
    }

    // Right rotation
    static Node rightRotate(Node y) {
        Node x = y.left;
        Node t2 = x.right;

        x.right = y;
        y.left = t2;

        y.height = 1 + max(height(y.left), height(y.right));
        x.height = 1 + max(height(x.left), height(x.right));

        return x;
    }

    // Left rotation
    static Node leftRotate(Node x) {
        Node y = x.right;
        Node t2 = y.left;

        y.left = x;
        x.right = t2;

        x.height = 1 + max(height(x.left), height(x.right));
        y.height = 1 + max(height(y.left), height(y.right));

        return y;
    }

    // Insert account number and balance the tree
    static Node insert(Node node, int accountNo) {
        if (node == null) {
            return new Node(accountNo);
        }

        if (accountNo < node.accountNo) {
            node.left = insert(node.left, accountNo);
        } else if (accountNo > node.accountNo) {
            node.right = insert(node.right, accountNo);
        } else {
            System.out.println("Account number already exists.");
            return node;
        }

        node.height = 1 + max(height(node.left), height(node.right));
        int balance = getBalance(node);

        // LL case
        if (balance > 1 && accountNo < node.left.accountNo) {
            return rightRotate(node);
        }

        // RR case
        if (balance < -1 && accountNo > node.right.accountNo) {
            return leftRotate(node);
        }

        // LR case
        if (balance > 1 && accountNo > node.left.accountNo) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // RL case
        if (balance < -1 && accountNo < node.right.accountNo) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    // Search account number
    static boolean search(Node node, int accountNo) {
        if (node == null) {
            return false;
        }

        if (node.accountNo == accountNo) {
            return true;
        }

        if (accountNo < node.accountNo) {
            return search(node.left, accountNo);
        }

        return search(node.right, accountNo);
    }

    // Find minimum node
    static Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Delete account and rebalance the tree
    static Node delete(Node node, int accountNo) {
        if (node == null) {
            return null;
        }

        if (accountNo < node.accountNo) {
            node.left = delete(node.left, accountNo);
        } else if (accountNo > node.accountNo) {
            node.right = delete(node.right, accountNo);
        } else {
            if (node.left == null || node.right == null) {
                Node child = (node.left != null) ? node.left : node.right;
                node = child;
            } else {
                Node successor = findMin(node.right);
                node.accountNo = successor.accountNo;
                node.right = delete(node.right, successor.accountNo);
            }
        }

        if (node == null) {
            return null;
        }

        node.height = 1 + max(height(node.left), height(node.right));
        int balance = getBalance(node);

        // LL case
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rightRotate(node);
        }

        // LR case
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // RR case
        if (balance < -1 && getBalance(node.right) <= 0) {
            return leftRotate(node);
        }

        // RL case
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    // Inorder traversal
    static void inorder(Node node) {
        if (node != null) {
            inorder(node.left);
            System.out.print(node.accountNo + " ");
            inorder(node.right);
        }
    }

    // Preorder traversal
    static void preorder(Node node) {
        if (node != null) {
            System.out.print(node.accountNo + " ");
            preorder(node.left);
            preorder(node.right);
        }
    }

    // Postorder traversal
    static void postorder(Node node) {
        if (node != null) {
            postorder(node.left);
            postorder(node.right);
            System.out.print(node.accountNo + " ");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice, accountNo;

        do {
            System.out.println("\n--- Banking AVL Tree Menu ---");
            System.out.println("1. Insert Account Number");
            System.out.println("2. Search Account Number");
            System.out.println("3. Delete Account Number");
            System.out.println("4. Inorder Traversal");
            System.out.println("5. Preorder Traversal");
            System.out.println("6. Postorder Traversal");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter account number: ");
                    accountNo = sc.nextInt();
                    root = insert(root, accountNo);
                    System.out.println("Insertion operation completed.");
                    break;

                case 2:
                    System.out.print("Enter account number to search: ");
                    accountNo = sc.nextInt();
                    System.out.println(search(root, accountNo)
                        ? "Account number found."
                        : "Account number not found.");
                    break;

                case 3:
                    System.out.print("Enter account number to delete: ");
                    accountNo = sc.nextInt();

                    if (search(root, accountNo)) {
                        root = delete(root, accountNo);
                        System.out.println("Account number deleted.");
                    } else {
                        System.out.println("Account number not found.");
                    }
                    break;

                case 4:
                    System.out.print("Inorder: ");
                    inorder(root);
                    System.out.println();
                    break;

                case 5:
                    System.out.print("Preorder: ");
                    preorder(root);
                    System.out.println();
                    break;

                case 6:
                    System.out.print("Postorder: ");
                    postorder(root);
                    System.out.println();
                    break;

                case 7:
                    System.out.println("Program terminated.");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 7);

        sc.close();
    }
}
