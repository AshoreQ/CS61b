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

    private boolean isPalinromeHelper(LinkedListDeque<Character> wordDeque,
                                      CharacterComparator cc) {
        if (wordDeque.size() == 1) {
            return true;
        } else {
            return cc.equalChars(wordDeque.removeFirst(), wordDeque.removeLast())
                    && isPalinromeHelper(wordDeque, cc);
        }
    }
    public boolean isPalindrome(String word, CharacterComparator cc) {
        LinkedListDeque<Character> wordDeque = (LinkedListDeque<Character>) wordToDeque(word);
        return isPalinromeHelper(wordDeque, cc);
    }
}
