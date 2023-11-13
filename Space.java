public class Space {
    private LivingThing occupant;
    private Treasure cache;


    public Space() {
        this.occupant = null;
        this.cache = null;
    }


    public Space(LivingThing occupant) {
        this.occupant = occupant;
        this.cache = null;
    }

    public Space(Treasure t){

    }

    public void setOccupant(LivingThing occupant) {
        this.occupant = occupant;
    }


    public LivingThing getOccupant() {
        return occupant;
    }



    public String getConsoleStr() {
        if (occupant == null) {
            return "-";
        } else {
            return occupant.getConsoleStr();
        }
    }

    public Treasure getCache() {
        return cache;
    }

    public void setCache(Treasure cache) {
        this.cache = cache;
    }
    //updated Getconsolestr that requires a boolean to be passed in
    public String getConsoleStr(boolean reveal) {
        //if reveal = true
        //only shows monsters and treasure if true
        if (reveal) {
            if (occupant != null) {
                return occupant.getConsoleStr();
            } else if (cache != null) {
                return cache.getConsoleStr();
            } else {
                return "-";
            }
            //if reveal = false
            //makes sure to always show the explorer
        } else {

            if (occupant instanceof Explorer) {
                return occupant.getConsoleStr();
            } else {
                return "-";
            }
        }
    }

}
