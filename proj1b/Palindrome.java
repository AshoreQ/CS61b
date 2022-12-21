public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> wordDeque = new LinkedListDeque<>();
        char temp;
        for (int i = 0; i < word.length(); ++i) {
            temp = word.charAt(i);
            wordDeque.addLast(temp);
        }
        return wordDeque;
    }

    private boolean isPalinromeHelper(LinkedListDeque<Character> wordDeque) {
        if (wordDeque.isEmpty() || wordDeque.size() == 1) {
            return true;
        } else {
            return (wordDeque.removeFirst() == wordDeque.removeLast())
                    && isPalinromeHelper(wordDeque);
        }
    }
    public boolean isPalindrome(String word) {
        LinkedListDeque<Character> wordDeque = (LinkedListDeque<Character>) wordToDeque(word);
        return isPalinromeHelper(wordDeque);
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> deque = wordToDeque(word);
        while (!deque.isEmpty() && deque.size() != 1) {
            if (!cc.equalChars(deque.removeFirst(), deque.removeLast())) {
                return false;
            }
        }
        return true;
    }
}
