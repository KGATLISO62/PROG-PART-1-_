/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kgatl
 */

package javaapplicationproject.pkg1;

import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Message {

    private static int totalMessagesSent = 0;
    private static final String FILE_NAME = "messages.json";
    private static final Scanner ista = new Scanner(System.in);
    private static final Random random = new Random();

    String messageID;
    int messageNumber;
    String recipient;
    String message;
    String messageHash;
    String sender = "QuickChat User";

    // Constructor
    public Message(String messageID, int messageNumber,
                   String recipient, String message) {

        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    // Checks whether the message ID is valid
    public boolean checkMessageID() {
        return messageID != null
                && messageID.length() <= 10;
    }

    // Checks the recipient's South African phone number
    public String checkRecipientCell() {

        String regex = "^\\+27[0-9]{9}$";

        if (recipient != null && recipient.matches(regex)) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    // Checks that the message is not longer than 250 characters
    public String checkMessageLength() {

        if (message != null && message.length() <= 250) {
            return "Message ready to send.";
        }

        return "Message exceeds 250 characters. Please reduce message length.";
    }

    // Creates the message hash
    public String createMessageHash() {

        String firstWord = "";
        String lastWord = "";

        if (message != null && !message.trim().isEmpty()) {

            String[] words = message.trim().split("\\s+");

            firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
            lastWord = words[words.length - 1]
                    .replaceAll("[^a-zA-Z0-9]", "");
        }

        String firstTwo = messageID.substring(0, 2);

        messageHash = (firstTwo + ":" + messageNumber + ":"
                + firstWord + lastWord).toUpperCase();

        return messageHash;
    }

    // Sends, stores or disregards a message
    public String SentMessage(int choice) {

        if (messageHash == null) {
            createMessageHash();
        }

        if (choice == 1) {

            Arrays.sentMessages(this);
            Arrays.addMessageID(messageID);
            Arrays.addMessageHash(messageHash);

            totalMessagesSent++;

            return "Message successfully sent.";

        } else if (choice == 2) {

            return storeMessage();

        } else if (choice == 3) {

            Arrays.addDisregardedMessage(this);

            return "Message successfully discarded.";
        }

        return "Invalid message option.";
    }

    // Escapes special characters for JSON
    private static String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Converts escaped JSON characters back to normal text
    private static String unescapeJson(String value) {

        StringBuilder result = new StringBuilder();
        boolean escape = false;

        for (int i = 0; i < value.length(); i++) {

            char c = value.charAt(i);

            if (escape) {

                switch (c) {
                    case 'n':
                        result.append('\n');
                        break;
                    case 'r':
                        result.append('\r');
                        break;
                    case 't':
                        result.append('\t');
                        break;
                    case '"':
                        result.append('"');
                        break;
                    case '\\':
                        result.append('\\');
                        break;
                    default:
                        result.append(c);
                        break;
                }

                escape = false;

            } else if (c == '\\') {
                escape = true;
            } else {
                result.append(c);
            }
        }

        if (escape) {
            result.append('\\');
        }

        return result.toString();
    }

    // Reads one value from a JSON object
    private static String getJsonField(String object, String key) {

        String search = "\"" + key + "\":\"";
        int start = object.indexOf(search);

        if (start == -1) {
            return "";
        }

        start += search.length();

        StringBuilder value = new StringBuilder();
        boolean escape = false;

        for (int i = start; i < object.length(); i++) {

            char c = object.charAt(i);

            if (escape) {
                value.append('\\').append(c);
                escape = false;

            } else if (c == '\\') {
                escape = true;

            } else if (c == '"') {
                break;

            } else {
                value.append(c);
            }
        }

        return unescapeJson(value.toString());
    }

    // Separates individual JSON objects
    private static ArrayList<String> splitJsonObjects(String json) {

        ArrayList<String> records = new ArrayList<>();

        int depth = 0;
        int start = -1;
        boolean insideString = false;
        boolean escape = false;

        for (int i = 0; i < json.length(); i++) {

            char c = json.charAt(i);

            if (insideString) {

                if (escape) {
                    escape = false;
                } else if (c == '\\') {
                    escape = true;
                } else if (c == '"') {
                    insideString = false;
                }

            } else {

                if (c == '"') {
                    insideString = true;

                } else if (c == '{') {

                    if (depth == 0) {
                        start = i;
                    }

                    depth++;

                } else if (c == '}') {

                    depth--;

                    if (depth == 0 && start != -1) {
                        records.add(json.substring(start, i + 1));
                        start = -1;
                    }
                }
            }
        }

        return records;
    }

    // Saves all stored messages to the JSON file
    private static void saveStoredMessages() throws IOException {

        try (FileWriter writer = new FileWriter(FILE_NAME)) {

            writer.write("[\n");

            for (int i = 0; i < Arrays.storedMessages.size(); i++) {

                Message m = Arrays.storedMessages.get(i);

                writer.write("  {");
                writer.write("\"messageID\":\"" + escapeJson(m.messageID) + "\",");
                writer.write("\"messageHash\":\"" + escapeJson(m.messageHash) + "\",");
                writer.write("\"sender\":\"" + escapeJson(m.sender) + "\",");
                writer.write("\"recipient\":\"" + escapeJson(m.recipient) + "\",");
                writer.write("\"message\":\"" + escapeJson(m.message) + "\"");
                writer.write("}");

                if (i < Arrays.storedMessages.size() - 1) {
                    writer.write(",");
                }

                writer.write("\n");
            }

            writer.write("]");
        }
    }

    // Stores a message
    public String storeMessage() {

        try {

            if (messageHash == null) {
                createMessageHash();
            }

            readStoredMessages();

            Arrays.addStoredMessage(this);

            if (!Arrays.messageID.contains(messageID)) {
                Arrays.addMessageID(messageID);
            }

            if (!Arrays.messagesHash.contains(messageHash)) {
                Arrays.addMessageHash(messageHash);
            }

            saveStoredMessages();

            return "Message successfully stored.";

        } catch (IOException e) {

            return "Error storing message.";
        }
    }

    // Reads stored messages from the JSON file
    public static void readStoredMessages() throws IOException {

        if (!Files.exists(Paths.get(FILE_NAME))) {
            return;
        }

        String json = new String(
                Files.readAllBytes(Paths.get(FILE_NAME)));

        Arrays.storedMessages.clear();

        ArrayList<String> records = splitJsonObjects(json);

        for (String record : records) {

            String id = getJsonField(record, "messageID");
            String hash = getJsonField(record, "messageHash");
            String sender = getJsonField(record, "sender");
            String recipient = getJsonField(record, "recipient");
            String text = getJsonField(record, "message");

            Message stored = new Message(
                    id,
                    Arrays.storedMessages.size() + 1,
                    recipient,
                    text
            );

            stored.messageHash = hash;
            stored.sender = sender;

            Arrays.addStoredMessage(stored);

            if (!Arrays.messageID.contains(id)) {
                Arrays.addMessageID(id);
            }

            if (!Arrays.messagesHash.contains(hash)) {
                Arrays.addMessageHash(hash);
            }
        }
    }

    // Displays sender and recipient of all stored messages
    public static String displayStoredSendersAndRecipients() {

        StringBuilder result = new StringBuilder();

        for (Message m : Arrays.storedMessages) {

            result.append("Sender: ").append(m.sender)
                    .append("\nRecipient: ").append(m.recipient)
                    .append("\n\n");
        }

        return result.toString();
    }

    // Finds the longest stored message
    public static String longestStoredMessage() {

        if (Arrays.storedMessages.isEmpty()) {
            return "No stored messages available.";
        }

        Message longest = Arrays.storedMessages.get(0);

        for (Message m : Arrays.storedMessages) {

            if (m.message.length() > longest.message.length()) {
                longest = m;
            }
        }

        return longest.message;
    }

    // Searches for a message using its ID
    public static String searchByMessageID(String id) {

        for (Message m : Arrays.storedMessages) {

            if (m.messageID.equals(id)) {

                return "Recipient: " + m.recipient
                        + "\nMessage: " + m.message;
            }
        }

        return "Message ID not found.";
    }

    // Searches for all messages belonging to a recipient
    public static String searchByRecipient(String recipient) {

        StringBuilder result = new StringBuilder();

        for (Message m : Arrays.storedMessages) {

            if (m.recipient.equals(recipient)) {

                result.append(m.message).append("\n");
            }
        }

        if (result.length() == 0) {
            return "No messages found for this recipient.";
        }

        return result.toString();
    }

    // Deletes a stored message using its hash
    public static String deleteByHash(String hash) {

        for (int i = 0; i < Arrays.storedMessages.size(); i++) {

            Message m = Arrays.storedMessages.get(i);

            if (m.messageHash.equals(hash)) {

                String deletedMessage = m.message;

                Arrays.storedMessages.remove(i);

                try {
                    saveStoredMessages();

                    return "Message: '" + deletedMessage
                            + "' successfully deleted.";

                } catch (IOException e) {

                    return "Error deleting message.";
                }
            }
        }

        return "Message hash not found.";
    }

    // Displays a report of all stored messages
    public static String displayStoredReport() {

        StringBuilder result = new StringBuilder();

        for (Message m : Arrays.storedMessages) {

            result.append("Message ID: ").append(m.messageID)
                    .append("\nMessage Hash: ").append(m.messageHash)
                    .append("\nSender: ").append(m.sender)
                    .append("\nRecipient: ").append(m.recipient)
                    .append("\nMessage: ").append(m.message)
                    .append("\n\n");
        }

        return result.toString();
    }

    // Stored messages menu
    public static void storedMessagesMenu() {

        try {
            readStoredMessages();
        } catch (IOException e) {
            System.out.println("Unable to read stored messages.");
        }

        boolean running = true;

        while (running) {

            System.out.println("\n=== STORED MESSAGES ===");
            System.out.println("1. Display senders and recipients");
            System.out.println("2. Display longest stored message");
            System.out.println("3. Search by message ID");
            System.out.println("4. Search messages by recipient");
            System.out.println("5. Delete message using hash");
            System.out.println("6. Display full stored message report");
            System.out.println("7. Return to main menu");
            System.out.print("Choose an option: ");

            String option = ista.nextLine();

            switch (option) {

                case "1":
                    System.out.println(displayStoredSendersAndRecipients());
                    break;

                case "2":
                    System.out.println(longestStoredMessage());
                    break;

                case "3":
                    System.out.print("Enter message ID: ");
                    System.out.println(searchByMessageID(ista.nextLine()));
                    break;

                case "4":
                    System.out.print("Enter recipient number: ");
                    System.out.println(searchByRecipient(ista.nextLine()));
                    break;

                case "5":
                    System.out.print("Enter message hash: ");
                    System.out.println(deleteByHash(ista.nextLine()));
                    break;

                case "6":
                    System.out.println(displayStoredReport());
                    break;

                case "7":
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }

    // Main QuickChat menu
    public static void displayInfo() {

        boolean running = true;

        while (running) {

            System.out.println("\n=== QUICKCHAT ===");
            System.out.println("1. Send Message");
            System.out.println("2. Coming Soon");
            System.out.println("3. Quit");
            System.out.println("4. Stored Messages");
            System.out.print("Choose an option: ");

            String choice = ista.nextLine();

            switch (choice) {

                case "1":
                    sendMessage();
                    break;

                case "2":
                    System.out.println("Coming Soon.");
                    break;

                case "3":
                    running = false;
                    System.out.println("Goodbye!");
                    break;

                case "4":
                    storedMessagesMenu();
                    break;

                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }

    // Allows the user to send messages
    public static void sendMessage() {

        System.out.print("How many messages would you like to enter? ");

        int number;

        try {
            number = Integer.parseInt(ista.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            return;
        }

        for (int i = 0; i < number; i++) {

            String id = String.format("%010d",
                    random.nextInt(1000000000));

            System.out.print("Enter recipient cell number: ");
            String recipient = ista.nextLine();

            Message temp = new Message(id, i + 1, recipient, "");

            if (!temp.checkRecipientCell().startsWith(
                    "Cell phone number successfully")) {

                System.out.println(temp.checkRecipientCell());
                continue;
            }

            System.out.print("Write a message (under 250 characters): ");
            String text = ista.nextLine();

            Message msg = new Message(id, i + 1, recipient, text);

            if (!msg.checkMessageLength().equals("Message ready to send.")) {

                System.out.println(msg.checkMessageLength());
                continue;
            }

            msg.createMessageHash();

            System.out.println("\n1. Send message");
            System.out.println("2. Store message");
            System.out.println("3. Disregard message");
            System.out.print("Choose an option: ");

            try {

                int option = Integer.parseInt(ista.nextLine());

                System.out.println(msg.SentMessage(option));

            } catch (NumberFormatException e) {

                System.out.println("Invalid option.");
            }
        }

        System.out.println("Total messages sent: " + totalMessagesSent);
    }
}
 