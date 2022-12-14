/**
 * Exercise to showcase the step over button.
 * Code adapted from https://stackoverflow.com/questions/4895173/bitwise-multiply-and-add-in-java and https://stackoverflow.com/questions/1533131/what-useful-bitwise-operator-code-tricks-should-a-developer-know-about
 */
public class DebugExercise2 {
    /** Returns the max of a and b. Do not step into this function. 
      * This function may have a bug, but if it does, you should find it
      * by stepping over, not into. */
    public static int min(int a, int b) {
        int w = (b - a) >> 31;
        /* If you're stepping into this function, click the
           step out button because you're not going to learn anything. */
        int z = ~(b - a) >> 31;

        int min = b & w | a & z;
        return min;
    }


    /** Returns the sum of a and b. Do not step into this function. 
      * This function may have a bug, but if it does, you should find it
      * by stepping over, not into. */

    public static int add(int a, int b) {
        int x = a, y = b;
        /* If you're stepping into this function, click the
           step out button because you're not going to learn anything. */
        int xor, and, temp;
        and = x & y;
        xor = x ^ y;

        while (and != 0) {
            and <<= 1;
            temp = xor ^ and;
            and &= xor;
            xor = temp;
        }
        return xor;
    }

    /** Returns a new array where entry i is the max of
     * a[i] and b[i]. For example, if a = {1, -10, 3}
     * and b = {0, 20, 5}, this function will return {1, 20, 5}.
     * */
    public static int[] arrayMin(int[] a, int[] b) {
        if (a.length != b.length) {
            System.out.println("ERROR! Arrays don't match");
            return null;
        }
        int[] returnArray = new int[a.length];
        for (int i = 0; i < a.length; i += 1) {
            int minnerValue = min(a[i], b[i]);
            returnArray[i] = minnerValue;
        }

        return returnArray;
    }

    /** Returns the sum of all elements in x. */
    public static int arraySum(int[] x) {
        int i = 0;
        int sum = 0;
        while (i < x.length) {
            sum = sum + x[i];
            i = i + 1;
        }
        return sum;
    }

    /** Returns the sum of the element-wise max of a and b.
     *  For example if a = {2, 0, 10, 14} and b = {-5, 5, 20, 30},
     *  the result should be 57.
     * */
    public static int sumOfElementwiseMins(int[] a, int[] b) {
        int[] mins = arrayMin(a, b);
        int sumofMins = arraySum(mins);
        return sumofMins;
    }


    public static void main(String[] args) {
        int[] a = {1, 11, -1, -11};
        int[] b = {3, -3, 2, -1};

        int sumOfElementwiseMins = sumOfElementwiseMins(a, b), sumOfElement = 0;
        for (int i = 0; i < a.length; ++i) {
            sumOfElement = sumOfElement + a[i] + b[i];
        }
        System.out.println(sumOfElement - sumOfElementwiseMins);
    }
}
