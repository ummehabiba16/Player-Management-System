package com.uhl.playerdb.model;

import com.uhl.playerdb.service.ClubService;
import com.uhl.playerdb.service.ConsoleInputService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchClubs extends MainMenu{
    private MenuContext menuContext;
    private ClubService clubService;
    private Map<Integer, Runnable> functions= new HashMap<>();
    private Club currentClub;
    static final List<String> options = Arrays.asList(
            "Player(s) with the maximum salary of a club",
            "Player(s) with the maximum age of a club",
            "Player(s) with the maximum height of a club",
            "Total yearly salary of a club",
            "Find clubwise position Count and Countrywise Count",
            "Back to Main"
    );
    
    public SearchClubs(MenuContext menuContext){
        super(menuContext);
        this.menuContext = menuContext;
        clubService = menuContext.getClubService();
        functions.put(1, () -> maxSalary());
        functions.put(2, () -> maxAge());
        functions.put(3, () -> maxHeight());
        functions.put(4, () -> totalSalary());
        functions.put(5, () -> findClubwisePosCntAndCntrywiseCnt());
        functions.put(6, () -> menuContext.setMenu(new MainMenu(menuContext)));
    }



    @Override
    public void display() {
        int i = 1;
        for(String option : options) {
            System.out.println("\n\t("+i + ") " + option);
            i++;
        }
    }

    @Override
    public void call(int n) {
        //System.out.println("call of "+ getClass().getSimpleName()+" "+n );
        if(n < 1 || n > options.size()){
            System.out.println("Invalid option number");
            return;
        }
        if(n != options.size()){
            Club c = findClub();
            if(c == null){
                System.out.println("No such club with this name");
                return;
            }
        }
        functions.get(n).run();
    }

    private Club findClub(){
        System.out.println("Enter Club name: ");
        String clubName = ConsoleInputService.getInputString();
        Club c = clubService.getClubByName(clubName);
        this.currentClub = c;
        return c;
    }

    private void totalSalary() {
        System.out.println("Total salary : " + clubService.getTotalSalary(currentClub));

    }

    private void maxHeight() {
        PlayerList pList = clubService.maxHeight(currentClub);
        if(!pList.isEmpty()){
            pList.display();
        }
    }

    private void maxAge() {
        PlayerList pList = clubService.maxAge(currentClub);
        if(!pList.isEmpty()){
            pList.display();
        }
    }

    private void maxSalary() {
        PlayerList pList = clubService.maxSalary(currentClub);
        if(!pList.isEmpty()){
            pList.display();
        }
    }

    private void findClubwisePosCntAndCntrywiseCnt() {
        Map<Position, Integer> positionwiseList = clubService.getPosCount(currentClub);
        for(Map.Entry<Position,Integer> entry : positionwiseList.entrySet()){
            System.out.println(entry.getKey() + " : " + entry.getValue());
            List<Player> pl = currentClub.findByPosition(entry.getKey());
            Map<String, Integer> playerCount = clubService.findCountrywiseCount(pl);
            for(Map.Entry<String,Integer> entry2 : playerCount.entrySet()){
                System.out.println("\t" + entry2.getKey() + " : " + entry2.getValue());
            }
        }
    }


}
