package com.kerware.simulateurreusine;

public class Simulateur implements ICalculateurImpot {

    private int revenuNetDeclarant1;
    private int revenuNetDeclarant2;
    private SituationFamiliale situationFamiliale;
    private int nbEnfantsACharge;
    private int nbEnfantsSituationHandicap;
    private boolean parentIsole;

    private double revenuFiscalReference;
    private double abattement;
    private double nombreParts;
    private double impotAvantDecote;
    private double impotNet;
    private double decote;
    private double contributionExceptionnelle;

    @Override
    public void setRevenusNetDeclarant1(int revenuNetDeclarant1) {
        this.revenuNetDeclarant1 = revenuNetDeclarant1;
    }

    @Override
    public void setRevenusNetDeclarant2(int revenuNetDeclarant2) {
        this.revenuNetDeclarant2 = revenuNetDeclarant2;
    }

    @Override
    public void setSituationFamiliale(SituationFamiliale situationFamiliale) {
        this.situationFamiliale = situationFamiliale;
    }

    @Override
    public void setNbEnfantsACharge(int nbEnfantsACharge) {
        this.nbEnfantsACharge = nbEnfantsACharge;
    }

    @Override
    public void setNbEnfantsSituationHandicap(int nbEnfantsSituationHandicap) {
        this.nbEnfantsSituationHandicap = nbEnfantsSituationHandicap;
    }

    @Override
    public void setParentIsole(boolean parentIsole) {
        this.parentIsole = parentIsole;
    }

    @Override
    public double getNbPartsFoyerFiscal() {
        return nombreParts;
    }

    @Override
    public double getAbattement() {
        return abattement;
    }

    @Override
    public double getImpotSurRevenuNet() {
        return impotNet;
    }

    @Override
    public void calculImpotSurRevenuNet() {
        validerEntrees();
        calculerAbattement();
        calculerRevenuFiscalReference();
        calculerNombreDeParts();
        calculerContributionExceptionnelle();
        calculerImpotBrut();
        appliquerPlafonnement();
        appliquerDecote();
        calculerImpotNet();
    }

    private void validerEntrees() {
        if (revenuNetDeclarant1 < 0 || revenuNetDeclarant2 < 0) {
            throw new IllegalArgumentException("Le revenu net ne peut pas être négatif");
        }
        if (nbEnfantsACharge < 0) {
            throw new IllegalArgumentException("Le nombre d'enfants ne peut pas être négatif");
        }
        if (nbEnfantsSituationHandicap < 0) {
            throw new IllegalArgumentException("Le nombre d'enfants handicapés ne peut pas être négatif");
        }
        if (situationFamiliale == null) {
            throw new IllegalArgumentException("La situation familiale ne peut pas être null");
        }
        if (nbEnfantsSituationHandicap > nbEnfantsACharge) {
            throw new IllegalArgumentException("Le nombre d'enfants handicapés ne peut pas être supérieur au nombre d'enfants");
        }
        if (nbEnfantsACharge > 7) {
            throw new IllegalArgumentException("Le nombre d'enfants ne peut pas être supérieur à 7");
        }
        if (parentIsole && (situationFamiliale == SituationFamiliale.MARIE || situationFamiliale == SituationFamiliale.PACSE)) {
            throw new IllegalArgumentException("Un parent isolé ne peut pas être marié ou pacsé");
        }
        if ((situationFamiliale == SituationFamiliale.CELIBATAIRE || situationFamiliale == SituationFamiliale.DIVORCE || situationFamiliale == SituationFamiliale.VEUF) && revenuNetDeclarant2 > 0) {
            throw new IllegalArgumentException("Un célibataire, un divorcé ou un veuf ne peut pas avoir de revenu pour le déclarant 2");
        }
    }

    private void calculerAbattement() {
        double abt1 = Math.round(revenuNetDeclarant1 * Constants.TAUX_ABATTEMENT);
        double abt2 = Math.round(revenuNetDeclarant2 * Constants.TAUX_ABATTEMENT);

        abt1 = Math.min(Math.max(abt1, Constants.ABATTEMENT_MIN), Constants.ABATTEMENT_MAX);
        if (situationFamiliale == SituationFamiliale.MARIE || situationFamiliale == SituationFamiliale.PACSE) {
            abt2 = Math.min(Math.max(abt2, Constants.ABATTEMENT_MIN), Constants.ABATTEMENT_MAX);
        }

        abattement = abt1 + abt2;
    }

    private void calculerRevenuFiscalReference() {
        revenuFiscalReference = revenuNetDeclarant1 + revenuNetDeclarant2 - abattement;
        if (revenuFiscalReference < 0) {
            revenuFiscalReference = 0;
        }
    }

    private void calculerNombreDeParts() {
        double partsDeclarants = 0;
        switch (situationFamiliale) {
            case CELIBATAIRE:
            case DIVORCE:
            case VEUF:
                partsDeclarants = 1;
                break;
            case MARIE:
            case PACSE:
                partsDeclarants = 2;
                break;
        }

        double partsEnfants = nbEnfantsACharge <= 2 ? nbEnfantsACharge * 0.5 : 1.0 + (nbEnfantsACharge - 2);
        if (parentIsole && nbEnfantsACharge > 0) {
            partsEnfants += 0.5;
        }
        if (situationFamiliale == SituationFamiliale.VEUF && nbEnfantsACharge > 0) {
            partsEnfants += 1;
        }
        partsEnfants += nbEnfantsSituationHandicap * 0.5;

        nombreParts = partsDeclarants + partsEnfants;
    }

    private void calculerContributionExceptionnelle() {
        contributionExceptionnelle = 0;
        double[] tauxCEHR = (nombreParts == 1) ? Constants.TAUX_CEHR_CELIBATAIRE : Constants.TAUX_CEHR_COUPLE;

        for (int i = 0; i < Constants.LIMITE_CEHR.length - 1; i++) {
            if (revenuFiscalReference >= Constants.LIMITE_CEHR[i] && revenuFiscalReference < Constants.LIMITE_CEHR[i + 1]) {
                contributionExceptionnelle += (revenuFiscalReference - Constants.LIMITE_CEHR[i]) * tauxCEHR[i];
                break;
            } else {
                contributionExceptionnelle += (Constants.LIMITE_CEHR[i + 1] - Constants.LIMITE_CEHR[i]) * tauxCEHR[i];
            }
        }

        contributionExceptionnelle = Math.round(contributionExceptionnelle);
    }

    private void calculerImpotBrut() {
        double revenuImposable = revenuFiscalReference / nombreParts;
        impotAvantDecote = 0;

        for (int i = 0; i < Constants.LIMITE_TRANCHES.length - 1; i++) {
            if (revenuImposable >= Constants.LIMITE_TRANCHES[i] && revenuImposable < Constants.LIMITE_TRANCHES[i + 1]) {
                impotAvantDecote += (revenuImposable - Constants.LIMITE_TRANCHES[i]) * Constants.TAUX_IMPOSITION[i];
                break;
            } else {
                impotAvantDecote += (Constants.LIMITE_TRANCHES[i + 1] - Constants.LIMITE_TRANCHES[i]) * Constants.TAUX_IMPOSITION[i];
            }
        }

        impotAvantDecote = Math.round(impotAvantDecote * nombreParts);
    }

    private void appliquerPlafonnement() {
        double baisseImpot = impotAvantDecote - impotNet;
        double plafond = ((nombreParts - nombreParts) / 0.5) * Constants.PLAFOND_DEMI_PART;

        if (baisseImpot >= plafond) {
            impotNet = impotAvantDecote - plafond;
        }
    }

    private void appliquerDecote() {
        decote = 0;
        if (nombreParts == 1 && impotNet < Constants.SEUIL_DECOTE_SEUL) {
            decote = Constants.DECOTE_MAX_SEUL - (impotNet * Constants.TAUX_DECOTE);
        } else if (nombreParts == 2 && impotNet < Constants.SEUIL_DECOTE_COUPLE) {
            decote = Constants.DECOTE_MAX_COUPLE - (impotNet * Constants.TAUX_DECOTE);
        }

        decote = Math.round(decote);
        if (impotNet <= decote) {
            decote = impotNet;
        }

        impotNet -= decote;
    }

    private void calculerImpotNet() {
        impotNet += contributionExceptionnelle;
        impotNet = Math.round(impotNet);
    }
}
