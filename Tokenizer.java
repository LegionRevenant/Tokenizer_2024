import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tokenizer {
    // Token types
    enum TokenType {
        Word, Punctuation, Number, EndOfLine
    }

    // Token class to hold token value and type
    class Token {
        String value;
        TokenType type;

        Token(String value, TokenType type) {
            this.value = value;
            this.type = type;
        }

        @Override
        public String toString() {
            return "Token: \"" + value + "\" - Type: " + type;
        }
    }

    private String delimiter;

    public Tokenizer(String delimiter) {
        this.delimiter = delimiter;
    }

    // Check if the character is punctuation
    private boolean isPunctuation(char c) {
        return "!,.?;:".indexOf(c) != -1;
    }

    // Check if the string is a number
    private boolean isNumber(String s) {
        return s.matches("\\d+|\\d+\\.\\d+");
    }

    // Tokenize the input string
    public List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        StringBuilder temp = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (c == '\n') {
                tokens.add(new Token("\\n", TokenType.EndOfLine));
                continue;
            }

            if (String.valueOf(c).equals(delimiter) || Character.isWhitespace(c)) {
                addToken(temp.toString(), tokens);
                temp.setLength(0);
            } else if (isPunctuation(c)) {
                addToken(temp.toString(), tokens);
                tokens.add(new Token(String.valueOf(c), TokenType.Punctuation));
                temp.setLength(0);
            } else {
                temp.append(c);
            }
        }

        if (temp.length() > 0) {
            addToken(temp.toString(), tokens);
        }

        return tokens;
    }

    // Add token based on its type
    private void addToken(String token, List<Token> tokens) {
        if (!token.isEmpty()) {
            if (isNumber(token)) {
                tokens.add(new Token(token, TokenType.Number));
            } else {
                tokens.add(new Token(token, TokenType.Word));
            }
        }
    }

    // Display the granular breakdown
    public void displayGranularBreakdown(List<Token> tokens) {
        for (Token token : tokens) {
            if (token.type == TokenType.Word || token.type == TokenType.Number) {
                System.out.print("Token: \"" + token.value + "\" -> ");
                for (char c : token.value.toCharArray()) {
                    System.out.print("'" + c + "', ");
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // User input
        String delimiter = " "; // Default delimiter is space
        System.out.println("Delimiter : Space");

        System.out.print("Enter your text: ");
        String input = scanner.nextLine();

        Tokenizer tokenizer = new Tokenizer(delimiter);
        List<Token> tokens = tokenizer.tokenize(input);

        // Phase 1 Output
        System.out.println("Phase 1 Output:");
        for (Token token : tokens) {
            System.out.println(token);
        }

        // Phase 2 Output
        System.out.println("===================================================");
        System.out.println("Phase 2 Output (Granular Breakdown):");
        tokenizer.displayGranularBreakdown(tokens);
    }
}