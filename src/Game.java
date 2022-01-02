import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.Stack;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */

public class Game
{
    private Parser parser;
    private Room currentRoom;
    private Player player;
    private Room previousRoom;
    private Stack<Room> roomStack;
    private Random random=new Random();


        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        player= new Player("player");
        createRooms();
        parser = new Parser();



    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room ocean,sluice, canal, portArthur, beach,island,buoy,triangleOfBermuda,lighthouse,portNapoleon;
        Item promoboard, ashtray, cookie;
      
        // create the rooms
        ocean = new Room("in the middle of the ocean");
       sluice = new Room("in a sluice waiting for the sea level to rise");
        island = new Room("on an island");
        canal = new Room("in a narrow canal");
        portArthur = new Room("docked in port arthur");
        portNapoleon = new Room("docked in port Napoleon");
        buoy = new Room("sailing near a bouy");
        beach = new Room("on the beach");
        triangleOfBermuda = new Room("sailing in a mist near a place called triangle of bermuda");
        lighthouse = new Room("sailing near lighthouse where i can see light on top");


        // create the items
        promoboard = new Item("promoboard","University promoboard", 2.3);
        ashtray = new Item("ashtray","big yellow ashtray",4.6);
        cookie = new Item("cookie","delicious cookie",0.5);


        // add items to rooms
        island.addItem(promoboard);
        ocean.addItem(ashtray);
        ocean.addItem(cookie);



        // initialise room exits
        ocean.setExit("port",buoy);
        ocean.setExit("backwards", beach);
        ocean.setExit("starboard",portArthur);
        ocean.setExit("forwards",sluice);
        sluice.setExit("backwards", ocean);
        sluice.setExit("starboard", canal);
        island.setExit("forwards",beach);
        beach.setExit("backwards", island);
        beach.setExit("forwards", ocean);
        canal.setExit("port", sluice);
        canal.setExit("backwards", portArthur);
        portArthur.setExit("port", ocean);
        portArthur.setExit("forwards", canal);
        buoy.setExit("backwards", triangleOfBermuda);
        buoy.setExit("port", lighthouse);
        buoy.setExit("starboard", ocean);
        lighthouse.setExit("starboard", buoy);
        lighthouse.setExit("forwards", portNapoleon);
        portNapoleon.setExit("backwards",lighthouse);
        int number=random.nextInt(9);
        switch (number) {
            case 0 -> triangleOfBermuda.setExit("forwards", buoy);
            case 1 -> triangleOfBermuda.setExit("forwards", ocean);
            case 2 -> triangleOfBermuda.setExit("forwards", island);
            case 3 -> triangleOfBermuda.setExit("forwards", portNapoleon);
            case 4 -> triangleOfBermuda.setExit("forwards", portArthur);
            case 5 -> triangleOfBermuda.setExit("forwards", canal);
            case 6 -> triangleOfBermuda.setExit("forwards", sluice);
            case 7 -> triangleOfBermuda.setExit("forwards", lighthouse);
            case 8 -> triangleOfBermuda.setExit("forwards", beach);
        }


        player.setCurrentRoom(ocean);  // start game ocean

    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP.getWord() + "' if you need help.");
        System.out.println();
        printLocationInfo();
    }

    private void printLocationInfo() {
        System.out.print(player.getInfo());
        System.out.println();
        System.out.println();
    }






    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command)
    {
        boolean wantToQuit = false;



        CommandWord commandWord = command.getCommandWord();
        switch (commandWord){
            case HELP:
                printHelp();
                break;
            case TAKE:
                take(command);
                break;
            case GO:
                goRoom(command);
                break;
            case LOOK:
                look(command);
                break;
            case DROP:
                drop(command);
                break;
            case EAT:
                eat(command);
                break;
            case QUIT:
                wantToQuit=quit(command);
                break;
            case BACK:
                back(command);
                break;
            case STACKBACK:
                stackBack(command);
                break;

            default:
                System.out.println("I don't know what you mean...");
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the
     * command words.
     */
    private void printHelp()
    {
        System.out.println("Player " + player.getName() + " is lost and alone and wanders");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Possible command words are:");
        System.out.println(parser.showCommands());
        System.out.println();
    }

    /**
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();
        Room nextRoom = player.getCurrentRoom().getExit(direction);
        previousRoom = player.getCurrentRoom();
       // roomStack.push(player.getCurrentRoom());


        // Try to leave current room.
        if (!player.go(direction)) {
            System.out.println("There is no door!");
        } else {

            player.setCurrentRoom(nextRoom);
            printLocationInfo();
        }
    }

    private void look(Command command)
    {
        String commandWordSecond= command.getSecondWord();
        if(!command.hasSecondWord()){
            printLocationInfo();
        }
        else if (commandWordSecond.equals("room")){
            System.out.println(player.getCurrentRoom().getLongDescription());
        }
        else if (commandWordSecond.equals("player")){
            System.out.println(player.playerinfo());
        }


    }

    private void back(Command command)

    {

        if(command.hasSecondWord()) {

            System.out.println("Back what?");

            return;

        }

        if(previousRoom == null) {

            System.out.println("you can not go back.");

            return;

        }

        player.setCurrentRoom(previousRoom);



        System.out.println("You are back at your previous location.");

        printLocationInfo();

    }

    private void stackBack(Command command)

    {

        if(command.hasSecondWord())

        {

            System.out.println("stackBack what?");

            return;

        }

        if (previousRoom == null)

        {

            System.out.println("Sorry, cannot go stackBack.");

            return;

        }



        player.setCurrentRoom(roomStack.pop());

        System.out.println("You have gone stackBack:");

        printLocationInfo();

    }

    private void take(Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know what to take...
            System.out.println("Take what?");
            return;
        }
        String itemName = command.getSecondWord();
        if (player.take(itemName)) {
            printLocationInfo();

        } else {
            System.out.println("There is no item here with the name " + itemName);
        }
    }

    private void drop(Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know what to take...
            System.out.println("Drop what?");
            return;
        }
        String itemName = command.getSecondWord();
        if (player.drop(itemName)) {
            printLocationInfo();
        } else {
            System.out.println("There is no item here with the name " + itemName);
        }
    }

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    private void eat(Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know what to take...
            System.out.println("eat what?");
            return;
        }
        String itemName= command.getSecondWord();
        if (player.eat(itemName)){
            System.out.println("You ate a magic cookie and can now carry 5 kg more.");
            printLocationInfo();
        } else {
            System.out.println("you can't eat this");
        }


    }

    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}
