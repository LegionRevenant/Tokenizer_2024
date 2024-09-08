import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MyTokenizer {

    public static void main(String[] args) {
        // Create a Scanner object for user input
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the input string:");

        // Read the input string from the user
        String input = scanner.nextLine();

        // Close the scanner
        scanner.close();

        // Phase 1: Tokenization
        List<Token> tokens = tokenize(input);
        System.out.println("INPUT STRING: " + input);
        for (Token token : tokens) {
            System.out.println(token);
        }

        // Phase 2: Granular Breakdown
        System.out.println("\nGranular Breakdown:");
        granularBreakdown(tokens);
    }

    // Token class to hold the token value and its type
    static class Token {
        String value;
        String type;

        Token(String value, String type) {
            this.value = value;
            this.type = type;
        }

        @Override
        public String toString() {
            return "TOKEN: \"" + value + "\" - Type: " + type;
        }
    }

    // Tokenize the input string
    public static List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();

        // Check if the input contains a delimiter
        if (input.contains(";")) {
            // Split the string by newlines
            String[] lines = input.split("\n");

            for (String line : lines) {
                if (line.isEmpty()) {
                    // Skip empty lines (they are likely the result of trailing newlines)
                    tokens.add(new Token("/n", "End of Line"));
                    continue;
                }

                // Split each line by the delimiter (semicolon)
                String[] parts = line.split(";");

                for (String part : parts) {
                    // Tokenize each part
                    tokenizePart(part, tokens);
                }

                // Add an "End of Line" token for each newline
                tokens.add(new Token("End of Line", "End of Line"));
            }
        } else {
            // If no delimiter is present, treat the whole input as one token
            tokenizePart(input, tokens);
        }
        return tokens;
    }

    // Classify tokens from each part
    private static void tokenizePart(String part, List<Token> tokens) {
        StringBuilder currentToken = new StringBuilder();
        char[] chars = part.toCharArray();
        boolean hasDecimal = false;
        boolean hasLetter = false;

        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];

            if (Character.isLetter(ch)) {
                currentToken.append(ch);
                hasLetter = true;
            } else if (Character.isDigit(ch)) {
                currentToken.append(ch);
            } else if (ch == '.') {
                if (i > 0 && Character.isDigit(chars[i - 1]) && i < chars.length - 1 && Character.isDigit(chars[i + 1])) {
                    currentToken.append(ch); // Append period in decimal numbers
                    hasDecimal = true;
                } else {
                    if (currentToken.length() > 0) {
                        addToken(currentToken.toString(), tokens); // End previous token
                        currentToken.setLength(0); // Clear token buffer
                    }
                    tokens.add(new Token(String.valueOf(ch), "Punctuation"));
                }
            } else if (isPunctuation(ch)) {
                if (currentToken.length() > 0) {
                    addToken(currentToken.toString(), tokens); // End previous token
                    currentToken.setLength(0); // Clear token buffer
                }
                tokens.add(new Token(String.valueOf(ch), "Punctuation"));
            }
        }

        if (currentToken.length() > 0) {
            addToken(currentToken.toString(), tokens); // End last token
        }
    }

    // Helper method to classify and add a token
    private static void addToken(String token, List<Token> tokens) {
        if (token.matches("[a-zA-Z]+")) {
            tokens.add(new Token(token, "Word"));
        } else if (token.matches("\\d+")) {
            tokens.add(new Token(token, "Number"));
        } else if (token.matches("\\d+\\.\\d+")) {
            tokens.add(new Token(token, "Number")); // Handle decimal numbers
        } else if (token.matches("[a-zA-Z0-9]+")) {
            tokens.add(new Token(token, "Alphanumeric"));
        } else if (token.matches("\\d+\\.\\d+[a-zA-Z]+") || token.matches("[a-zA-Z]+\\d+\\.\\d+")) {
            tokens.add(new Token(token, "Alphanumeric")); // Handle decimal + word
        }
    }

    // Check if a character is punctuation (including @, #, $, %, ^, &, *, ')
    private static boolean isPunctuation(char ch) {
        return ch == ',' || ch == '!' || ch == '?' || ch == '-' || ch == '.' || ch == '%' || ch == '$' ||
                ch == '@' || ch == '#' || ch == '^' || ch == '&' || ch == '*' || ch == '\'';
    }

    // Phase 2: Granular Breakdown
    public static void granularBreakdown(List<Token> tokens) {
        for (Token token : tokens) {
            // Skip punctuation and End of Line tokens in granular breakdown
            if (!token.type.equals("Punctuation") && !token.type.equals("End of Line")) {
                System.out.print("TOKEN breakdown of: \"" + token.value + "\" -> ");
                for (char c : token.value.toCharArray()) {
                    System.out.print("'" + c + "', ");
                }
                System.out.println();
            }
        }
    }
}
