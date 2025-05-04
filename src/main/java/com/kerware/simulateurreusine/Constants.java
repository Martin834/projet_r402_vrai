package com.kerware.simulateurreusine;

public class Constants {
    // Limites des tranches de revenus imposables
    public static final int[] LIMITE_TRANCHES = {0, 11294, 28797, 82341, 177106, Integer.MAX_VALUE};

    // Taux d'imposition par tranche
    public static final double[] TAUX_IMPOSITION = {0.0, 0.11, 0.3, 0.41, 0.45};

    // Limites des tranches pour la contribution exceptionnelle sur les hauts revenus
    public static final int[] LIMITE_CEHR = {0, 250000, 500000, 1000000, Integer.MAX_VALUE};

    // Taux de la contribution exceptionnelle sur les hauts revenus pour les célibataires
    public static final double[] TAUX_CEHR_CELIBATAIRE = {0.0, 0.03, 0.04, 0.04};

    // Taux de la contribution exceptionnelle sur les hauts revenus pour les couples
    public static final double[] TAUX_CEHR_COUPLE = {0.0, 0.0, 0.03, 0.04};

    // Abattement
    public static final int ABATTEMENT_MAX = 14171;
    public static final int ABATTEMENT_MIN = 495;
    public static final double TAUX_ABATTEMENT = 0.1;

    // Plafond de baisse maximal par demi-part
    public static final double PLAFOND_DEMI_PART = 1759;

    // Seuils et taux de décote
    public static final double SEUIL_DECOTE_SEUL = 1929;
    public static final double SEUIL_DECOTE_COUPLE = 3191;
    public static final double DECOTE_MAX_SEUL = 873;
    public static final double DECOTE_MAX_COUPLE = 1444;
    public static final double TAUX_DECOTE = 0.4525;
}
