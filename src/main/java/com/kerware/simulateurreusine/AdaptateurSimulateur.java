package com.kerware.simulateurreusine;

public class AdaptateurSimulateur implements ICalculateurImpot {
    private final Simulateur simulateur;

    public AdaptateurSimulateur() {
        this.simulateur = new Simulateur();
    }

    @Override
    public void setRevenusNetDeclarant1(int revenuNetDeclarant1) {
        simulateur.setRevenusNetDeclarant1(revenuNetDeclarant1);
    }

    @Override
    public void setRevenusNetDeclarant2(int revenuNetDeclarant2) {
        simulateur.setRevenusNetDeclarant2(revenuNetDeclarant2);
    }

    @Override
    public void setSituationFamiliale(SituationFamiliale situationFamiliale) {
        simulateur.setSituationFamiliale(situationFamiliale);
    }

    @Override
    public void setNbEnfantsACharge(int nbEnfantsACharge) {
        simulateur.setNbEnfantsACharge(nbEnfantsACharge);
    }

    @Override
    public void setNbEnfantsSituationHandicap(int nbEnfantsSituationHandicap) {
        simulateur.setNbEnfantsSituationHandicap(nbEnfantsSituationHandicap);
    }

    @Override
    public void setParentIsole(boolean parentIsole) {
        simulateur.setParentIsole(parentIsole);
    }

    @Override
    public double getNbPartsFoyerFiscal() {
        return simulateur.getNbPartsFoyerFiscal();
    }

    @Override
    public double getAbattement() {
        return simulateur.getAbattement();
    }

    @Override
    public double getImpotSurRevenuNet() {
        return simulateur.getImpotSurRevenuNet();
    }

    @Override
    public void calculImpotSurRevenuNet() {
        simulateur.calculImpotSurRevenuNet();
    }
}
