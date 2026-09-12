# Gabarit LaTeX pour les documents de fin d’études DI - UdeS

Modèle pour les documents de fin d’études du département d'informatique de l'Université de Sherbrooke.

Les documents de fins d’études du DI incluent les

- Thèses (Ph.D.)
- Mémoires (M.Sc.)
- Essais et rapports de stage (maitrises de type cours)

Le style est défini dans la classe `scienceUdeS` qui est dans le répertoire `style` :

```latex
\documentclass[options]{style/scienceUdeS}
```

## Options de la classe

#### Langue du document

- `francais` : langue par défaut
- `english`  : modifie la langue du document à l’anglais. Les sections en français requises par les règlements facultaires sont inclus dans cette version. Il est possible aussi avec cette option d’ajouter la version anglaise du titre du document à la suite du titre français (voir fichier `contenu/prelim.tex`.

Cette option permet de changer les nomenclatures globalement ainsi que la gestion de la coupure de mots.

Il est possible pour un document globalement en français d’avoir des sections en anglais ou vice-versa. Dans ce cas, pour adapter les éléments de ces sections spécifiques (nomenclature et gestion de coupure des mots), il suffira d’ajouter

```latex
\modeAnglais (ou \englishMode)
```

ou 

```latex
\modeFrancais (ou \frenchMode)
```

juste devant la section qui est dans la deuxième langue. Il suffira d’ajouter 

```latex
\modeDefaut (ou \defaultMode)
```

pour revenir à la langue par défaut juste après la section en question.

####Type de document

Le gabarit est *par défaut* pour un mémoire de maitrise. Pour passer à un format différent, il suffit d’utiliser une des quatre options suivantes : 

- `memoire`  : format pour un mémoire de maitrise de type recherche (M.Sc.), par défaut
- `these` : format pour une thèse de doctorat (Ph.D.)
  - `cotutelle`  : pour une thèse en cotutelle (Ph.D.), change la page couverture

- `essai` : format pour un essai (maitrise de type cours) 
- `rapport` : format pour un rapport de stage (maitrise de type cours)

Aussi disponible

- `enRedaction` : option pour signaler l'état du document (*à retirer pour la version finale*)
- `final`  : option pour le dépôt final, par défaut

#### Absence d’éléments

si votre document ne contient pas les éléments suivants, utiliser ces options éliminera les pages de listes pertinentes  dans les pages préliminaires.

- `pasDeFigure` : le document ne contient pas de figures (à ne pas utiliser si vous avez des figures dans le contenu);

- `pasDeTableau` : le document ne contient pas de tableaux (à ne pas utiliser si vous avez des tableaux dans le contenu)
- `pasDAlgo` : le document ne contient pas d'algorithmes
- `pasDAbreviation` : le document ne contient pas d'abréviations
- `pasDeCode` : le document ne contient pas de codes source

#### Autres

- `nonatbib`  : pour ne pas inclure le paquet `natbib`, par défaut il sera inclus. Non recommandé pour utiliser les styles de bibliographies fournies avec le gabarit.
- `bibliothequeNationale` : présente le document pour la copie de la  bibliothèque nationale.
- `hypertexte`
- `caractereUtf`  : utilisation des caractères UTF (utile pour les accents)
- `caractereLatin` : utilisation des caractères `latin1`, par défaut

La classe `scienceUdeS` est basée sur la classe `book`.  Toutes les options de la classe `book` peuvent être utilisées **sauf** pour changer la grosseur de la police de base (fixé à `12pt`) ainsi que le papier (fixé à `letterpaper`).

## Autres informations importantes

### Versions latex testées

Fonctionne sur Overleaf avec compilateur pdflatex texlive 21, texlive 20. Fonctionne avec texlive 2022 sur mac (texstudio).

Pour faire fonctionner adéquatement le paquet `glossaries-extra` dans Overleaf, il faut absolument que le fichier principal (modele.tex) se trouve à la racine du projet Overleaf et non dans un sous répertoire.

![image-20230502134729789](parame%CC%80tres%20overleaf.png)

Prévu pour compiler avec pdflatex et non pdf (option du package hyperref)

"pdflatex" -synctex=1 -interaction=nonstopmode %.tex

### Ajouts de paquets ou d’autres éléments utilitaires

Vous pouvez ajouter des paquets ou des définitions ou autres qui devraient être avant `\begin{document}` dans le fichier `ajoutsFct.tex`. Vous trouverez d’ailleurs dans les commentaires de ce fichier la liste des paquets inclus dans la classe.

### Identification du document

Le fichier `contenu/prelim.tex` contient les commandes à emplir pour la page titre, le sommaire, les mots-clés, les remerciements et la liste des abbréviations. Dans le cas d’une cotutelle se trouvent également le département, l’institution partenaire  ainsi que le grade à obtenir. Ces dernières commandes n’auront d’effet que si l’option `cotutelle` est activée dans la classe.

### Page de jury

Pour un mémoire de maitrise ou une thèse de doctorat, vous pouvez entrer votre jury dans le fichier `contenu/jury.tex`. Celui-ci contient des commandes pour entrer les différents membres selon le rôle qu’ils ou elles assument dans le jury (membre, direction, co-direction, présidence). Il y a des versions masculines et féminines pour la direction, codirection et présidence selon votre jury.

Ne changez pas l’ordre suivant:

1. Direction;
2. Codirection.s s’il y a lieu;
3. Membre externe (obl. pour une thèse);
4. Membre interne;
5. Présidence.

Lors du dépôt final, l’apparence de la page sera modifiée (date et acceptation du document) en autant que vous enleviez l’option `enRedaction` de la classe.

### Annexes du gabarit

Le fichier `annexe/annexe-SpecLatex.tex` du gabarit contient des exemples d’utilisations de paquets et de commandes latex diverses. Le fichier `annexe/annexe-Bibtex.tex` contient des exemples pour l’utilisation de bibtex.

### Style de bibliographie

Le style de la bibliographie peut être modifié directement dans le fichier principal `modele.tex`. Le présent gabarit offre deux styles propres au DI : `UdeSDIfr` pour une thèse rédigée en français et `UdeSDIeng` pour une thèse rédigée en anglais. Voir le fichier `modele.tex` pour la syntaxe.  Les deux styles sont inclus dans le sous-répertoire `style/`.

Les deux styles sont relativement semblables hormis l’élément linguistique. Entre autres, ils sont paramétrés pour reformater automatiquement la liste d’auteurs ou autrices qui dépasse sept (voir guide de rédaction) sans avoir à le faire explicitement dans le fichier `.bib` contenant vos notices. 

Le style `UdeSDIfr` requiert d’inclure le fichier `UdeSDIfrbst.tex` quelque part dans votre fichier principal. De même, le style `UdeSDIeng` requiert d’inclure le fichier `UdeSDIengbst.tex` . Ces fichiers contiennent la définition des termes français et anglais respectivement, des notices bibliographiques et peuvent être édités (inclus dans le sous-répertoire `style/`).

Vous pouvez utiliser un autre style de bibliographie. Si le style de bibliographie choisi ne supporte pas le paquet `natbib`, la classe offre l’option `nonatbib` pour éviter de l’inclure (voir fichier `modele.tex`).
