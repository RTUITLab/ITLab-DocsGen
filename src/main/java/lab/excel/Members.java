package lab.excel;

public class Members {
    public class  Events{
        private String titleEvent;
        private String dateEvent;
        private String wasEvent;

        public void setTitleEvent(String titleEvent){this.titleEvent = titleEvent;}
        public void setDateEvent(String dateEvent){this.dateEvent = dateEvent;}
        public void setWasEvent(String wasEvent){this.wasEvent = wasEvent;}
    }

    private String name;
    private String surname;
    private Events[] titleDateEvents = new Events[10];

    public String getName(){return this.name;}
    public String getSurname(){return this.surname;}
    public Events getTitleDateEvents(int i){return this.titleDateEvents[i];}

    public void setName(String name){this.name = name;}
    public void setSurName(String surname){this.surname = surname;}

    public void setTitleDateEvents(String title, String date, String wasEvent, int i){
            this.titleDateEvents[i].setTitleEvent(title);
            this.titleDateEvents[i].setDateEvent(date);
            this.titleDateEvents[i].setWasEvent(wasEvent);
    }

}
