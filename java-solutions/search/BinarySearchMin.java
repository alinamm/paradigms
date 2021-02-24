package search;

public class BinarySearchMin {
    public static void main(String[] args) {
        int n = args.length;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(args[i]);
        }
        System.out.println(binarySearch1(a, -1, n));
        //System.out.println(binarySearch2(a));
    }

    //Pred: left >= -1 && right > left && a.length > 0 && right <= a.length && есть 2 возрастающие части массива, элементы одной из которых, меньше элементов другой
    //Post: R -- минимальное значение в массиве
    static int binarySearch1(int[] a, int left, int right) {
        //Pred: left >= -1 && a.length > 0 && right <= a.length && right > left

        //left >= -1 && a.length > 0 && right <= a.length && right > left
        int mid = (right + left) / 2;
        //mid = (right + left) / 2 && left >= -1 && a.length > 0 && right <= a.length && right > left

        //mid = (right + left) / 2 && a.length > 0 && left >= -1 && right <= a.length && right > left
        if (a.length == 1) {
            //mid = (right + left) / 2 && a.length == 1 && left >= -1 && right <= a.length && right > left
            return a[0];
        }

        //mid = (right + left) / 2 && right > left && a.length > 1 && left >= -1 && right <= a.length && right > left
        if (right - left == 1) {
            //a.length > 1 && left >= -1 && mid = (right + left) / 2 && right - left == 1 && right <= a.length
            if (right == a.length) {
                //a.length > 1 && left >= -1 && mid = (right + left) / 2 && right - left == 1 && right == a.length
                return a[a.length - 1];
            }
            //a.length > 1 && left >= -1 && mid = (right + left) / 2 && right - left == 1 && right < a.length
            return a[right];
        }
        //a.length > 1 && left >= -1 && mid = (right + left) / 2 && right - left > 1 && right <= a.length
        if (a[mid] < a[a.length - 1]) {
            //a.length > 0 && left >= -1 && mid = (right + left) / 2 && right - left > 1 && right <= a.length && a[mid] < a[a.length - 1] -> min слева от a[mid]
            //Pred: left >= -1 && mid > left && a.length > 0 && right <= a.length
            //Post: R -- минимальное значение в массиве
            return binarySearch1(a, left, mid);
        } else {
            //a.length > 0 && left >= -1 && mid = (right + left) / 2 && right - left > 1 && right <= a.length && a[mid] >= a[a.length - 1] -> min справа от a[mid]
            //Pred: mid >= -1 && right > mid && a.length > 0 && right <= a.length
            //Post: R -- минимальное значение в массиве
            return binarySearch1(a, mid, right);
        }
    }


    //Pred: a.length > 0 && есть 2 возрастающие части массива, элементы одной из которых, меньше элементов другой
    //Post: R -- минимальное значение в массиве
    static int binarySearch2(int[] a) {
        //Pred: a.length > 0

        //a.length > 0
        int left = -1;
        //a.length > 0 && left == -1
        int right = a.length;
        //a.length > 0 && left == -1 && right == a.length && right > left


        //a.length > 0 && left == -1 && right == a.length && right > left
        if (a.length == 1) {
            //a.length == 1 && left == -1 && right == a.length && right > left
            return a[0];
        }
        //a.length > 1 && left == -1 && right == a.length && right > left
        //Inv: a[left] > a[a.length - 1] && a[right] <= a[a.length - 1]
        while (right - left > 1) {
            //(a[left] > a[a.length - 1] && a[right] <= a[a.length - 1]) && a.length > 1 && right - left > 1
            int mid = (right + left) / 2;
            //(a[left] > a[a.length - 1] && a[right] <= a[a.length - 1]) && mid = (right + left) / 2 && a.length > 1 && right - left > 1
            if (a[mid] > a[a.length - 1]) {
                //(a[left] > a[a.length - 1] && a[right] <= a[a.length - 1]) && mid = (right + left) / 2 && a[mid] > a[a.length - 1] && a.length > 1 && right - left > 1
                left = mid;
                //(a[left] > a[a.length - 1] && a[right] <= a[a.length - 1]) && a[mid] > a[a.length - 1] && a.length > 1 && left == mid
            } else {
                //(a[left] > a[a.length - 1] && a[right] <= a[a.length - 1]) && mid = (right + left) / 2 && a[mid] <= a[a.length - 1] && a.length > 1 && right - left > 1
                right = mid;
                //(a[left] > a[a.length - 1] && a[right] <= a[a.length - 1]) && a[mid] <= a[a.length - 1] && a.length > 1 && right == mid
            }
        }
        //a[left] > a[a.length - 1] && a[right] <= a[a.length - 1] && right - left == 1 -> a[right] -- минимальное значение в массиве
        return a[right];
    }
}

