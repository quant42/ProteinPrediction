package proteinprediction.prediction;

/**
 *
 * @author spoeri
 */
public class SummarizedPrediction {
     public String seq;
     boolean isProtein;
     double isProtConv;
     String inMembran;
     String inMembranConv;
     String insideOutside;
     String insideOutsideConv;
     String tmhTml;
     String tmhtmlConv;
     
     public SummarizedPrediction() {}
     public SummarizedPrediction(String seq, boolean isProtein, double isProtConv, String inMembran, String inMembranConv, String insideOutside, String insideOutsideConv, String tmhTml, String tmhtmlConv) {
         this.seq = seq;
         this.isProtein = isProtein;
         this.isProtConv = isProtConv;
         this.inMembran = inMembran;
         this.inMembranConv = inMembranConv;
         this.insideOutside = insideOutside;
         this.insideOutsideConv = insideOutsideConv;
         this.tmhTml = tmhTml;
         this.tmhtmlConv = tmhtmlConv;
     }
}
