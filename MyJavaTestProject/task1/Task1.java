import java.util.*;

public class Task1 {
    public static void main(String[] args) {
        Hangman hangman = new Hangman();
        hangman.start();
    }
}

class Hangman {

    private String word;
    private char[] guessedWord;
    private int lives = 3;
    private Scanner sc;

    public Hangman() {
        this.word = guessWord();
        this.guessedWord = new char[word.length()];
        Arrays.fill(guessedWord, '_');
        this.lives = 3;
        this.sc = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to Hangman!");
        while (lives > 0 && !isWon()) {
            printInfo();
            char letter = getLetter();
            guess(letter);
        }
        printResult();
    }

    private void printInfo() {
        System.out.println("\nLives: " + lives);
        for (char c : guessedWord) {
            System.out.print(c + " ");
        }
        System.out.println();
    }

    private char getLetter() {
        while (true) {
            System.out.print("\nEnter letter: ");
            String inputStr = sc.next().trim().toLowerCase();
            if (inputStr.length() != 1) {
                System.out.println("Please enter exactly one letter!");
                continue;
            }
            return inputStr.charAt(0);
        }
    }

    private void guess(char letter) {
        boolean found = false;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == letter) {
                guessedWord[i] = letter;
                found = true;
            }
        }
        if (!found) {
            lives--;
            System.out.println("\n------------------");
            System.out.println("No such letter!");
            System.out.println("------------------\n");
        }
    }

    private boolean isWon() {
        return Arrays.equals(guessedWord, word.toCharArray());
    }

    private void printResult() {
        if (isWon()) {
            System.out.println("\nYou win! The word is: " + word);
        } else {
            System.out.println("You lost! The hidden word was: " + word);
        }
    }

    private String guessWord() {
        List<String> words = new ArrayList<>(List.of(
                "cat",
                "dog",
                "car",
                "table",
                "window",
                "pen",
                "computer",
                "book",
                "tree",
                "phone",
                "city",
                "apple"));
        Random rand = new Random();
        return words.get(rand.nextInt(words.size()));
    }
}
