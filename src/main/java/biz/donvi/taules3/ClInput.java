package biz.donvi.taules3;

import biz.donvi.taules3.Taules;
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

    private Map<String, Consumer<String>> commands = new HashMap<>();

    public ClInput(Taules taules) {
        this.taules = taules;
        in = new Scanner(System.in);
        commands.put("help", this::cmdHelp);
        commands.put("update-guilds",this::cmdUpdateGuilds);
        commands.put("update-users", this::cmdUpdateUsers);
        commands.put("active-dir", this::cmdPrintActiveDir);
        commands.put("update-current-calls", this::cmdUpdateCurrentCalls);
        commands.put("plot-messages",this::generatePlot);

    }

    public void inputLoop() {
        while (taules.keepRunning()) try{
            getInput(in.nextLine());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void getInput(final String input) {
        String firstArg = input.contains(" ") ? input.substring(0, input.indexOf(" ") - 1) : input;
        commands.getOrDefault(firstArg, this::cmdNotFound).accept(input);
    }

    private void cmdNotFound(String input) {
        String firstArg = input.contains(" ") ? input.substring(0, input.indexOf(" ") - 1) : input;
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

    private void generatePlot(String args){
        new MessageGraph(1,1).generatePlot(taules.dataManager);
    }

}
