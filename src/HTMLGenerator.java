import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import java.io.*;
import java.util.Vector;

public class HTMLGenerator {

    private Vector<Statistics> statisticsVector;
    private int k;
    private double t;
    private int n;
    private int p;
    private int m;
    private int totalCounter;

    /**
     * Constructor for HTMLGenerator
     */
    public HTMLGenerator(Vector<Statistics> statisticsVector, int k, double t, int n, int p, int m, int totalCounter) {
        this.statisticsVector = statisticsVector;
        this.k = k;
        this.t = t;
        this.n = n;
        this.p = p;
        this.m = m;
        this.totalCounter = totalCounter;
    }

    /**
     *
     *
     */
    public void generateHTML() throws IOException {
        VelocityEngine velEngine = new VelocityEngine();
        velEngine.init();

        Template template = velEngine.getTemplate("/src/htmlPageTemplate.vm");
        VelocityContext context = new VelocityContext();
        StringWriter writer = new StringWriter();

        File file = new File("statistics\\index.html");
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter;
        if (file.exists()) {
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("<!DOCTYPE html>\r\n" +
                    "<html lang=\"es\">\r\n" +
                    "<head>\r\n" +
                    "<script>\r\n"+
                    "function show(shown, hidden) {\r\n"+
                    "document.getElementById(shown).style.display='block';\r\n"+
                    "document.getElementById(hidden).style.display='none';\r\n"+
                    "return false;}\r\n"+
                    "</script>\r\n"+
                    "<meta charset=\"UTF-8\">\r\n" +
                    "<title> Simulation Statistics </title>\r\n" +
                    "<link rel=\"stylesheet\" href=\"style.css\" type=\"text/css\"/>\r\n" +
                    "</head>\r\n" +
                    "<body>\r\n");

            for(int index = 0; index <= totalCounter; index++)
            {
                if(index<totalCounter){
                    writer = fillTemplate(template, context, writer, index, statisticsVector.get(index).getFinalClock());
                }
                else {
                    double divider = totalCounter*1.0;
                    writer = fillTemplate(template, context, writer, index, divider);
                }

            }
            bufferedWriter.write(writer.toString());

            bufferedWriter.write("</body>\r\n " +
                    "</html>");
        } else {
            file.createNewFile();
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("<!DOCTYPE html>\r\n" +
                    "<html lang=\"es\">\r\n" +
                    "<head>\r\n" +
                    "<script>\r\n"+
                    "function show(shown, hidden) {\r\n"+
                    "document.getElementById(shown).style.display='block';\r\n"+
                    "document.getElementById(hidden).style.display='none';\r\n"+
                    "return false;}\r\n"+
                    "</script>\r\n"+
                    "<meta charset=\"UTF-8\">\r\n" +
                    "<title> Simulation Statistics </title>\r\n" +
                    "<link rel=\"stylesheet\" href=\"style.css\" type=\"text/css\"/>\r\n" +
                    "</head>\r\n" +
                    "<body>\r\n");

            for(int index = 0; index <= totalCounter; index++)
            {
                if(index<totalCounter){
                    writer = fillTemplate(template, context, writer, index, statisticsVector.get(index).getFinalClock());
                }
                else {
                    double divider = totalCounter*1.0;
                    writer = fillTemplate(template, context, writer, index, divider);
                }

            }
            bufferedWriter.write(writer.toString());
        }
        bufferedWriter.close();
        String direction = new String("statistics\\index.html");
        Runtime.getRuntime().exec("cmd /c start "+ direction);
    }

    private StringWriter fillTemplate(Template template, VelocityContext context, StringWriter writer, int index, double divider){

        //context.put("runNumber", "Run" + (index+1));

        if(index == 0){
            context.put("divReference", "<div id=\"Run" + (index+1) + "\">");
            context.put("pageReference", "<a href=\"#\" onclick=\"return show('Run2','Run1');\"><div id=\"ShowPage\">Show page 2</div></a>");
            context.put("title", "Statistics of run 1");
        } else if(index == totalCounter)
        {
            context.put("divReference", "<div id=\"Run" + (index+1) + "\" style=\"display:none\">");
            context.put("pageReference", "<a href=\"#\" onclick=\"return show('Run" + (index) + "','Run" + (index + 1) + "');\"><div id=\"ShowPage\">Show page " + (index) + "</div></a>");
            context.put("title", "General statistics");
        } else
        {
            context.put("divReference", "<div id=\"Run" + (index+1) + "\" style=\"display:none\">");
            context.put("pageReference", "<a href=\"#\" onclick=\"return show('Run" + (index) + "','Run" + (index + 1) + "');\"><div id=\"ShowPage\">Show page " + index + "</div></a>\r\n" +
                    "<a href=\"#\" onclick=\"return show('Run" + (index + 2) + "','Run" + (index + 1) + "');\"><div id=\"ShowPage\">Show page " + (index + 2) + "</div></a>");
            context.put("title", "Statistics of run " + (index+1));
        }

        context.put("k", this.k);
        context.put("t", this.t);
        context.put("n", this.n);
        context.put("p", this.p);
        context.put("m", this.m);

        context.put("lambdaFromConnectionsModule", statisticsVector.get(index).getConnectionsModuleStatistics().getArrivalRate(divider));
        context.put("muFromConnectionsModule", statisticsVector.get(index).getConnectionsModuleStatistics().generateDepartureRate());
        context.put("rhoFromConnectionsModule", statisticsVector.get(index).getConnectionsModuleStatistics().getArrivalRate(divider) / (statisticsVector.get(index).getConnectionsModuleStatistics().generateDepartureRate()*this.k));
        context.put("LqFromConnectionsModule", statisticsVector.get(index).getConnectionsModuleStatistics().generateAverageQueueSize());
        context.put("LsFromConnectionsModule", statisticsVector.get(index).getConnectionsModuleStatistics().getAverageClientInService());
        context.put("LFromConnectionsModule", statisticsVector.get(index).getConnectionsModuleStatistics().generateAverageClientsInModule());
        context.put("WqFromConnectionsModule", statisticsVector.get(index).getConnectionsModuleStatistics().getAverageQueueTime());
        context.put("WsFromConnectionsModule", statisticsVector.get(index).getConnectionsModuleStatistics().getAverageServiceTime());
        context.put("WFromConnectionsModule", statisticsVector.get(index).getConnectionsModuleStatistics().getAverageTimeInModule());
        context.put("DDLFromConnectionsModule", statisticsVector.get(index).getConnectionsModuleStatistics().getAverageTimeInModulePerSentence(1));
        context.put("UPDATEFromConnectionsModule", statisticsVector.get(index).getConnectionsModuleStatistics().getAverageTimeInModulePerSentence(2));
        context.put("JOINFromConnectionsModule", statisticsVector.get(index).getConnectionsModuleStatistics().getAverageTimeInModulePerSentence(3));
        context.put("SELECTFromConnectionsModule", statisticsVector.get(index).getConnectionsModuleStatistics().getAverageTimeInModulePerSentence(4));
        context.put("idleTimeFromConnectionsModule", statisticsVector.get(index).getConnectionsModuleStatistics().getIdleTime());

        context.put("lambdaFromProcessModule", statisticsVector.get(index).getProcessModuleStatistics().getArrivalRate(divider));
        context.put("muFromProcessModule", statisticsVector.get(index).getProcessModuleStatistics().generateDepartureRate());
        context.put("rhoFromProcessModule", statisticsVector.get(index).getProcessModuleStatistics().getArrivalRate(divider) / statisticsVector.get(index).getProcessModuleStatistics().generateDepartureRate());
        context.put("LqFromProcessModule", statisticsVector.get(index).getProcessModuleStatistics().generateAverageQueueSize());
        context.put("LsFromProcessModule", statisticsVector.get(index).getProcessModuleStatistics().getAverageClientInService());
        context.put("LFromProcessModule", statisticsVector.get(index).getProcessModuleStatistics().generateAverageClientsInModule());
        context.put("WqFromProcessModule", statisticsVector.get(index).getProcessModuleStatistics().getAverageQueueTime());
        context.put("WsFromProcessModule", statisticsVector.get(index).getProcessModuleStatistics().getAverageServiceTime());
        context.put("WFromProcessModule", statisticsVector.get(index).getProcessModuleStatistics().getAverageTimeInModule());
        context.put("DDLFromProcessModule", statisticsVector.get(index).getProcessModuleStatistics().getAverageTimeInModulePerSentence(1));
        context.put("UPDATEFromProcessModule", statisticsVector.get(index).getProcessModuleStatistics().getAverageTimeInModulePerSentence(2));
        context.put("JOINFromProcessModule", statisticsVector.get(index).getProcessModuleStatistics().getAverageTimeInModulePerSentence(3));
        context.put("SELECTFromProcessModule", statisticsVector.get(index).getProcessModuleStatistics().getAverageTimeInModulePerSentence(4));
        context.put("idleTimeFromProcessModule", statisticsVector.get(index).getProcessModuleStatistics().getIdleTime());

        context.put("lambdaFromQueryProcessingModule", statisticsVector.get(index).getQueryProcessingStatistics().getArrivalRate(divider));
        context.put("muFromQueryProcessingModule", statisticsVector.get(index).getQueryProcessingStatistics().generateDepartureRate());
        context.put("rhoFromQueryProcessingModule", statisticsVector.get(index).getQueryProcessingStatistics().getArrivalRate(divider) / (statisticsVector.get(index).getQueryProcessingStatistics().generateDepartureRate()*this.n));
        context.put("LqFromQueryProcessingModule", statisticsVector.get(index).getQueryProcessingStatistics().generateAverageQueueSize());
        context.put("LsFromQueryProcessingModule", statisticsVector.get(index).getQueryProcessingStatistics().getAverageClientInService());
        context.put("LFromQueryProcessingModule", statisticsVector.get(index).getQueryProcessingStatistics().generateAverageClientsInModule());
        context.put("WqFromQueryProcessingModule", statisticsVector.get(index).getQueryProcessingStatistics().getAverageQueueTime());
        context.put("WsFromQueryProcessingModule", statisticsVector.get(index).getQueryProcessingStatistics().getAverageServiceTime());
        context.put("WFromQueryProcessingModule", statisticsVector.get(index).getQueryProcessingStatistics().getAverageTimeInModule());
        context.put("DDLFromQueryProcessingModule", statisticsVector.get(index).getQueryProcessingStatistics().getAverageTimeInModulePerSentence(1));
        context.put("UPDATEFromQueryProcessingModule", statisticsVector.get(index).getQueryProcessingStatistics().getAverageTimeInModulePerSentence(2));
        context.put("JOINFromQueryProcessingModule", statisticsVector.get(index).getQueryProcessingStatistics().getAverageTimeInModulePerSentence(3));
        context.put("SELECTFromQueryProcessingModule", statisticsVector.get(index).getQueryProcessingStatistics().getAverageTimeInModulePerSentence(4));
        context.put("idleTimeFromQueryProcessingModule", statisticsVector.get(index).getQueryProcessingStatistics().getIdleTime());

        context.put("lambdaFromTransactionsModule", statisticsVector.get(index).getTransactionsModuleStatistics().getArrivalRate(divider));
        context.put("muFromTransactionsModule", statisticsVector.get(index).getTransactionsModuleStatistics().generateDepartureRate());
        context.put("rhoFromTransactionsModule", statisticsVector.get(index).getTransactionsModuleStatistics().getArrivalRate(divider) / (statisticsVector.get(index).getTransactionsModuleStatistics().generateDepartureRate()*this.p));
        context.put("LqFromTransactionsModule", statisticsVector.get(index).getTransactionsModuleStatistics().generateAverageQueueSize());
        context.put("LsFromTransactionsModule", statisticsVector.get(index).getTransactionsModuleStatistics().getAverageClientInService());
        context.put("LFromTransactionsModule", statisticsVector.get(index).getTransactionsModuleStatistics().generateAverageClientsInModule());
        context.put("WqFromTransactionsModule", statisticsVector.get(index).getTransactionsModuleStatistics().getAverageQueueTime());
        context.put("WsFromTransactionsModule", statisticsVector.get(index).getTransactionsModuleStatistics().getAverageServiceTime());
        context.put("WFromTransactionsModule", statisticsVector.get(index).getTransactionsModuleStatistics().getAverageTimeInModule());
        context.put("DDLFromTransactionsModule", statisticsVector.get(index).getTransactionsModuleStatistics().getAverageTimeInModulePerSentence(1));
        context.put("UPDATEFromTransactionsModule", statisticsVector.get(index).getTransactionsModuleStatistics().getAverageTimeInModulePerSentence(2));
        context.put("JOINFromTransactionsModule", statisticsVector.get(index).getTransactionsModuleStatistics().getAverageTimeInModulePerSentence(3));
        context.put("SELECTFromTransactionsModule", statisticsVector.get(index).getTransactionsModuleStatistics().getAverageTimeInModulePerSentence(4));
        context.put("idleTimeFromTransactionsModule", statisticsVector.get(index).getTransactionsModuleStatistics().getIdleTime());

        context.put("lambdaFromExecutorModule", statisticsVector.get(index).getExecutorModuleStatistics().getArrivalRate(divider));
        context.put("muFromExecutorModule", statisticsVector.get(index).getExecutorModuleStatistics().generateDepartureRate());
        context.put("rhoFromExecutorModule", statisticsVector.get(index).getExecutorModuleStatistics().getArrivalRate(divider) / (statisticsVector.get(index).getExecutorModuleStatistics().generateDepartureRate()*this.m));
        context.put("LqFromExecutorModule", statisticsVector.get(index).getExecutorModuleStatistics().generateAverageQueueSize());
        context.put("LsFromExecutorModule", statisticsVector.get(index).getExecutorModuleStatistics().getAverageClientInService());
        context.put("LFromExecutorModule", statisticsVector.get(index).getExecutorModuleStatistics().generateAverageClientsInModule());
        context.put("WqFromExecutorModule", statisticsVector.get(index).getExecutorModuleStatistics().getAverageQueueTime());
        context.put("WsFromExecutorModule", statisticsVector.get(index).getExecutorModuleStatistics().getAverageServiceTime());
        context.put("WFromExecutorModule", statisticsVector.get(index).getExecutorModuleStatistics().getAverageTimeInModule());
        context.put("DDLFromExecutorModule", statisticsVector.get(index).getExecutorModuleStatistics().getAverageTimeInModulePerSentence(1));
        context.put("UPDATEFromExecutorModule", statisticsVector.get(index).getExecutorModuleStatistics().getAverageTimeInModulePerSentence(2));
        context.put("JOINFromExecutorModule", statisticsVector.get(index).getExecutorModuleStatistics().getAverageTimeInModulePerSentence(3));
        context.put("SELECTFromExecutorModule", statisticsVector.get(index).getExecutorModuleStatistics().getAverageTimeInModulePerSentence(4));
        context.put("idleTimeFromExecutorModule", statisticsVector.get(index).getExecutorModuleStatistics().getIdleTime());

        context.put("discardedConnections", statisticsVector.get(index).getQueryStatistics().getDiscardedConnections());
        context.put("totalConnections", statisticsVector.get(index).getQueryStatistics().getTotalConnections());
        context.put("averageConnectionsLifeTime", statisticsVector.get(index).getQueryStatistics().getTotalAverageConnectionLifeTime());

        template.merge(context, writer);

        return writer;
    }
}