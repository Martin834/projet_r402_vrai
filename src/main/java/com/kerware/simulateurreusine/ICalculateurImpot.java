package com.kerware.simulateurreusine;

public interface ICalculateurImpot {
    void setRevenusNetDeclarant1(int revenuNetDeclarant1);
    void setRevenusNetDeclarant2(int revenuNetDeclarant2);
    void setSituationFamiliale(SituationFamiliale situationFamiliale);
    void setNbEnfantsACharge(int nbEnfantsACharge);
    void setNbEnfantsSituationHandicap(int nbEnfantsSituationHandicap);
    void setParentIsole(boolean parentIsole);
    double getNbPartsFoyerFiscal();
    double getAbattement();
    double getImpotSurRevenuNet();
    void calculImpotSurRevenuNet();
}
