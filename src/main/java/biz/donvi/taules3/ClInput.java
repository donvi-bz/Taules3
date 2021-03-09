package biz.donvi.taules3;

import biz.donvi.taules3.graphing.CallGraph;
import biz.donvi.taules3.graphing.MessageGraph;
import biz.donvi.taules3.util.ConsoleColor;
import biz.donvi.taules3.util.Util;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class ClInput {
    private final Taules taules;
    private final Scanner in;

    private final Map<String, Consumer<String>> commands = new HashMap<>();

    public ClInput(Taules taules) {
        this.taules = taules;
        in = new Scanner(System.in);
        commands.put("help",                 this::cmdHelp);
        commands.put("l",                    null);
        commands.put("last",                 null);
        commands.put("active-dir",           this::cmdPrintActiveDir);
        commands.put("update-guilds",        this::cmdUpdateGuilds);
        commands.put("update-users",         this::cmdUpdateUsers);
        commands.put("update-current-calls", this::cmdUpdateCurrentCalls);
        commands.put("plot-messages",        this::generateMessagePlot);
        commands.put("plot-calls",           this::generateCallPlot);

    }

    public void inputLoop() {
        while (taules.keepRunning()) try{
            getInput(in.nextLine());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private String lastInput = "";

    private void getInput(String input) {
        if (input.equalsIgnoreCase("l") || input.equalsIgnoreCase("last")) {
            System.out.println("Running last input:\n" + lastInput);
            input = lastInput;
        }
        String firstArg = input.contains(" ") ? input.substring(0, input.indexOf(" ")) : input;
        commands.getOrDefault(firstArg, this::cmdNotFound).accept(input);
        lastInput = input;
    }

    private void cmdNotFound(String input) {
        String firstArg = input.contains(" ") ? input.substring(0, input.indexOf(" ")) : input;
        System.out.println(
            ConsoleColor.YELLOW +
            "Could not find command '" + firstArg + "' did you mean one of these?\n"  + ConsoleColor.RESET +
            Util.listOut(
            commands.keySet().stream().sorted(
                Comparator.comparingInt((val) -> {
                    double score = StringUtils.getLevenshteinDistance(val, firstArg);
                    if (val.contains(firstArg)) score = Math.sqrt(score);
                    return (int)score;
                }))
            .limit(3)
            .collect(Collectors.toList()))
        );
    }

    private void cmdHelp(String args) {
        System.out.println(
            "Command list: " + Util.listOut(commands.keySet().stream().sorted().collect(Collectors.toList())));
    }

    private void cmdUpdateGuilds(String args){
        System.out.println(
            "Updated guilds in database taking " +
            Util.timedCompletion(taules.dataManager::updateGuilds) +
            " milliseconds.");
    }

    private void cmdUpdateUsers(String args){
        System.out.println(
            "Updated users in database taking " +
            Util.timedCompletion(taules.dataManager::updateUsers) +
            " milliseconds.");
    }

    private void cmdPrintActiveDir(String args) {
        System.out.println(Paths.get("").toAbsolutePath());
    }

    private void cmdUpdateCurrentCalls(String args) {
        System.out.println(
            "Updated current call records taking " +
            Util.timedCompletion(taules.dataManager::updateAllCallRecords) +
            " milliseconds.");
    }

    private MessageGraph messageGraph = null;
    private void generateMessagePlot(String args){
        args = args.substring(args.indexOf(' ') + 1);
        boolean showUsage = false;
        if (args.startsWith("?")){
            System.out.println("Usage:");
            showUsage = true;
        } else if (args.startsWith("last")){
            if (messageGraph != null) messageGraph.displayPlot();
            else System.out.println("No currently stored message-plot exists.");
        } else {
            try {
                String guildName = args.substring(args.indexOf('"') + 1, args.lastIndexOf('"'));
                int averageOver = Integer.parseInt(args.substring(args.lastIndexOf(' ') + 1));
                System.out.println("Generating plot for '" + guildName + "' averaging over " + averageOver + " minutes...");
                messageGraph = new MessageGraph(1,1);
                messageGraph.generatePlot(taules.dataManager, guildName, averageOver);
            } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                System.out.println("Incorrect format. Proper usage:");
                showUsage = true;
            }
        }
        if (showUsage) System.out.println(
            ConsoleColor.YELLOW + "plot-messages \"<Guild Name>\" <average-over>" + ConsoleColor.RESET);

    }
    private CallGraph callGraph = null;
    private void generateCallPlot(String args){
        args = args.substring(args.indexOf(' ') + 1);
        if (args.startsWith("last")){
            if (callGraph != null) callGraph.displayPlot();
            else System.out.println("No currently stored call-plot exists.");
        } else {
            String guildName = args.substring(args.indexOf('"') + 1, args.lastIndexOf('"'));
            callGraph = new CallGraph(1, 1);
            callGraph.generatePlot(taules.dataManager, guildName, 1);
        }
    }

}
