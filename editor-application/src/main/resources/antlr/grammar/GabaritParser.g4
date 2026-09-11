parser grammar GabaritParser;

options {
  tokenVocab = GabaritLexer;
}

gabarit: regle*;

regle: objet contrainte valeurs? FR;

objet: SECTION |
       IDENTIFIANT;

contrainte: ORDONNER |
      COMPOSER |
      NOMMER   |
      UNIQUE;

valeurs: DV valeur+ FV;

valeur: mot separateur?;

mot: MOT;

separateur: SEPARATEUR;

