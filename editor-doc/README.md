# LaTeX template for DI end-of-studies documents - UdeS

Model for end-of-studies documents from the computer science department (CSD) of University of Sherbrooke.

CSD end-of-study documents include

- Theses (Ph.D.)
- Dissertations (M.Sc.)
- Essays and internship reports (course-type master's degrees)

The style is defined in the `scienceUdeS` class which is in the `style` directory :

```latex
\documentclass[options]{style/scienceUdeS}
```

## Options

#### Document language

- `francais` : default
- `english`  : changes the document language to English. The sections in French, as required by the faculty regulations are included in this version. It is also possible with this option to add the English version of the title of the document following the French title. (voir fichier `contenu/prelim.tex`).

This option allows you to change the nomenclatures globally as well as the management of hyphenation.

It is possible for a document that is generally in French to have sections in English or vice-versa. In this case, to adapt the elements of these specific sections (nomenclature and management of word hyphenation), it will suffice to add

```latex
\modeAnglais (or \englishMode)
```

or 

```latex
\modeFrancais (ou \frenchMode)
```

right in front of the section that is in the second language. Just add

```latex
\modeDefaut (ou \defaultMode)
```

to return to the default language immediately after the section in question.

####Type of document

The template’s *default* is a master's thesis. To switch to a different format, simply use one of the following four options: 

- `memoire`: format for a research-type master's thesis (M.Sc.), by default
- `these`: format for a doctoral thesis (Ph.D.)
  - `cotutelle`: for a thesis under joint supervision (Ph.D.), change the cover page

- `essay`: format for an essay (course-type master's degree)
- `report`: format for an internship report (course type master's degree)

Also available

- `enRedaction`: option to indicate the status of the document (*to be removed for the final version*)
- `final`: option for final submission, default

#### No elements

If your document does not contain the following elements, using these options will eliminate the relevant list pages in the preliminary pages.

- `pasDeFigure`: the document does not contain any figures (do not use if you have figures in the content);

- `pasDeTableau` : the document does not contain tables (do not use if you have tables in the content);
- `pasDAlgo` : the document does not contain algorithms
- `pasDAbreviation` : the document does not contain abbreviations
- `pasDeCode` : the document contains no source codes

#### Others

- `nonatbib`  : not to include the `natbib` package, by default it will be included. Not recommended if using the bibliography styles supplied with the template.
- `bibliothequeNationale` : presents the document for the national library copy.
- `hypertexte`
- `caractereUtf`  : using UTF characters (useful for accents)
- `caractereLatin` : using `latin1` characters, by default

The `scienceUdeS` class is based on the `book` class. All options in the `book` class can be used **except** to change the size of the base font (set to `12pt`) as well as the paper (set to `letterpaper`).

## Additional important information

### Latex versions tested

Works on Overleaf with pdflatex texlive 21 or texlive 20 compiler. Works with texlive 2022 on mac (texstudio).

To make the `glossaries-extra` package work properly in Overleaf, it is absolutely necessary that the main file (modele.tex) be located at the root of the Overleaf project and not in a subdirectory.

![image-20230502134729789](parame%CC%80tres%20overleaf.png)

Intended to compile with pdflatex and not pdf (hyperref package option).

"pdflatex" -synctex=1 -interaction=nonstopmode %.tex

### Additions of packages or other utility items

You can add packages or definitions or whatever that should be before `\begin{document}` in the `additionsFct.tex` file. You will also find in the comments of this file the list of packages included in the class.

### Document identification

The `content/prelim.tex` file contains the commands to fill in for the title page, the summary, the keywords, the acknowledgments and the list of abbreviations. In the case of joint supervision, there is also the department, the partner institution as well as the grade to be obtained. These last commands will only have effect if the `cotutelle` option is activated in the class.

### Jury page

For a master's thesis or a doctoral thesis, you can enter your jury in the `content/jury.tex` file. This contains commands to enter the different members according to the role they assume on the jury (member, management, co-direction, presidency). There are male and female versions for management, co-management and presidency depending on your jury.

Do not change the following order:

1. Direction;
2. Co-direction if applicable;
3. External member (required for a thesis);
4. Internal member;
5. Chairmanship.

During the final submission, the appearance of the page will be modified (date and acceptance of the document) as long as you remove the `enRedaction` option from the class.

### Template Appendices

The `annex/annex-SpecLatex.tex` file of the template contains examples of uses of various latex packages and commands. The file `annex/annex-Bibtex.tex` contains examples for using bibtex.

### Bibliography style

The bibliography style can be modified directly in the main `modele.tex` file. This template offers two DI-specific styles: `UdeSDIfr` for a thesis written in French and `UdeSDIeng` for a thesis written in English. See the `modele.tex` file for the syntax. Both styles are included in the `style/` subdirectory.

The two styles are relatively similar apart from the linguistic element. Among other things, they are configured to automatically reformat the list of authors that exceeds seven (see redaction guide) without having to do so explicitly in the `.bib` file containing your notices.

The `UdeSDIfr` style requires that you include the `UdeSDIfrbst.tex` file somewhere in your main file. Likewise, the `UdeSDIeng` style requires the `UdeSDIengbst.tex` file to be included. These files contain the definition of French and English terms respectively, bibliographic notices and can be edited (included in the `style/` subdirectory).

You can use another style of bibliography. If the chosen bibliography style does not support the `natbib` package, the class offers the option `nonatbib` to avoid including it (see `modele.tex` file).
