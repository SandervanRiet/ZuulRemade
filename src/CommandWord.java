public enum CommandWord {
    GO("go"),TAKE("take"),DROP("drop"),EAT("eat"),LOOK("look"),HELP("?"),QUIT("quit"),UNKNOWN(""),BACK("back"),STACKBACK("stackback");

    private String word;

    CommandWord(String word) {
        this.word=word;
    }

    public String getWord(){
        return word;
    }
}
