import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        String a = "racecar", b = "cat";
        boolean res1 = palindrome.isPalindrome(a), res2 = palindrome.isPalindrome(b);
        assertTrue(res1);
        assertFalse(res2);

        CharacterComparator offByOne = new OffByOne();
        assertTrue(palindrome.isPalindrome("a", offByOne));
        assertTrue(palindrome.isPalindrome("", offByOne));
        assertTrue(palindrome.isPalindrome("flake", offByOne));
        assertFalse(palindrome.isPalindrome("cat", offByOne));
    }
}
