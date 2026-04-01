package Doomsday;

import java.util.*;


class Item {
    private String name;
    private String description;
    private int points;
    private int weight;
    private boolean isPlayItem; // true if item has a purpose in the game

    public Item(String name, String description, int points, int weight, boolean isPlayItem) {
        this.name = name;
        this.description = description;
        this.points = points;
        this.weight = weight;
        this.isPlayItem = isPlayItem;
    }

    // Getters (no setters for name - items shouldn't change identity)
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description; // Description can change during game
    }

    public int getPoints() {
        return points;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isPlayItem() {
        return isPlayItem;
    }

    // Complete description including name, description, and weight
    public String getCompleteDescription() {
        return name + ": " + description + " (Weight: " + weight + ", Points: " + points + ")";
    }

    @Override
    public String toString() {
        return name;
    }
}

enum CommandEnum {
    GO("go"),
    HELP("help"),
    QUIT("quit"),
    LOOK("look"),
    BACK("back"),
    STATUS("status"),
    SCORE("score"),
    TURNS("turns"),
    TAKE("take"),
    DROP("drop"),
    INVENTORY("inventory"),
    EXAMINE("examine"),
    UNKNOWN("?");

    private String text;

    CommandEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}


class CommandWords {

    private static CommandEnum[] validCommands;

    static {
        validCommands = new CommandEnum[] {
            CommandEnum.GO,
            CommandEnum.HELP,
            CommandEnum.QUIT,
            CommandEnum.LOOK,
            CommandEnum.BACK,
            CommandEnum.STATUS,
            CommandEnum.SCORE,
            CommandEnum.TURNS,
            CommandEnum.TAKE,
            CommandEnum.DROP,
            CommandEnum.INVENTORY,
            CommandEnum.EXAMINE
        };
    }

    public static CommandEnum getCommand(String theString) {
        for (CommandEnum cmd : validCommands) {
            if (cmd.getText().equals(theString.toLowerCase())) {
                return cmd;
            }
        }
        return CommandEnum.UNKNOWN;
    }

    public String showAll() {
        StringBuilder sb = new StringBuilder();
        for (CommandEnum cmd : validCommands) {
            sb.append(cmd.getText()).append(" ");
        }
        return sb.toString();
    }
}


class Command {

    private CommandEnum commandWord;
    private String secondWord;

    public Command(CommandEnum commandWord, String secondWord) {
        this.commandWord = commandWord;
        this.secondWord = secondWord;
    }

    public CommandEnum getCommandWord() {
        return commandWord;
    }

    public String getSecondWord() {
        return secondWord;
    }

    public boolean hasSecondWord() {
        return secondWord != null;
    }
}


class Reader {

    private Scanner scanner;

    public Reader() {
        scanner = new Scanner(System.in);
    }

    public Command getCommand() {
        System.out.print("> ");
        String inputLine = scanner.nextLine().trim().toLowerCase();
        
        String[] words = inputLine.split("\\s+", 2);
        String word1 = words.length > 0 ? words[0] : "";
        String word2 = words.length > 1 ? words[1].trim() : null;

        CommandEnum command = CommandWords.getCommand(word1);
        return new Command(command, word2);
    }
}


class Room {

    private String name;
    private String description;
    private HashMap<String, Room> exits;
    private HashMap<String, Item> items; // HashMap for items by name
    private int points;

    public Room(String name, String description, int points) {
        this.name = name;
        this.description = description;
        this.points = points;
        exits = new HashMap<>();
        items = new HashMap<>();
    }

    public void setExit(String direction, Room neighbor) {
        exits.put(direction, neighbor);
    }

    public Room getExit(String direction) {
        return exits.get(direction);
    }

    public int getPoints() {
        return points;
    }

    // Add an item to this room
    public void addItem(Item item) {
        items.put(item.getName().toLowerCase(), item);
    }

    // Get an item from this room by its name
    public Item getItem(String name) {
        return items.get(name.toLowerCase());
    }

    // Remove an item from the room by name, returns the item or null
    public Item removeItem(String name) {
        return items.remove(name.toLowerCase());
    }

    // Check if room has an item
    public boolean hasItem(String name) {
        return items.containsKey(name.toLowerCase());
    }

    // Get all item names in this room
    public Collection<String> getItemNames() {
        return items.keySet();
    }

    // Get count of items
    public int getItemCount() {
        return items.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(name).append(" ===\n");
        sb.append(description).append("\n");

        if (!exits.isEmpty()) {
            sb.append("Exits: ");
            for (String dir : exits.keySet()) {
                sb.append(dir).append(" ");
            }
            sb.append("\n");
        }

        if (!items.isEmpty()) {
            sb.append("Items here: ");
            boolean first = true;
            for (String itemName : items.keySet()) {
                if (!first) sb.append(", ");
                sb.append(itemName);
                first = false;
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}


class Game {

    private Room currentRoom;
    private Room previousRoom;

    private int score = 0;
    private int turns = 0;
    private int health = 100;

    private final int MAX_CARRY_WEIGHT = 8; // Maximum items player can carry
    private HashMap<String, Item> inventory = new HashMap<>();
    private int currentWeight = 0;

    private Reader reader;

    public Game() {
        createRooms();
        reader = new Reader();
    }

    

    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}
