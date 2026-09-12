%%
%% This is file `scienceUdeS.cls',
%%
%% Version 2019/03/25 v2.1
%% Benoît Fraikin (benoit.fraikin@usherbrooke.ca)
%%
%% Version 2022/02/10 v3.0
%% Marie-Flavie Auclair-Fortier (m-f.auclair@usherbrooke.ca)
%%
%% ---------------------
%%
%% Copyright 1993 1994 1995 1996 1997 1998 1999 2000 2001 2002 2003 2004
%% The LaTeX3 Project and any individual authors listed elsewhere
%% in this file.
%%
%% --------------------------------------------------------------
%%
%% It may be distributed and/or modified under the
%% conditions of the LaTeX Project Public License, either version 1.3
%% of this license or (at your option) any later version.
%% The latest version of this license is in
%%    http://www.latex-project.org/lppl.txt
%% and version 1.3 or later is part of all distributions of LaTeX
%% version 2003/12/01 or later.
%%
%% This file has the LPPL maintenance status "maintained".
%%
%% The list of derived (unpacked) files belonging to the distribution
%% and covered by LPPL is defined by the unpacking scripts (with
%% extension .ins) which are part of the distribution.
%% \CharacterTable
%%  {Upper-case    \A\B\C\D\E\F\G\H\I\J\K\L\M\N\O\P\Q\R\S\T\U\V\W\X\Y\Z
%%   Lower-case    \a\b\c\d\e\f\g\h\i\j\k\l\m\n\o\p\q\r\s\t\u\v\w\x\y\z
%%   Digits        \0\1\2\3\4\5\6\7\8\9
%%   Exclamation   \!     Double quote  \"     Hash (number) \#
%%   Dollar        \$     Percent       \%     Ampersand     \&
%%   Acute accent  \'     Left paren    \(     Right paren   \)
%%   Asterisk      \*     Plus          \+     Comma         \,
%%   Minus         \-     Point         \.     Solidus       \/
%%   Colon         \:     Semicolon     \;     Less than     \<
%%   Equals        \=     Greater than  \>     Question mark \?
%%   Commercial at \@     Left bracket  \[     Backslash     \\
%%   Right bracket \]     Circumflex    \^     Underscore    \_
%%   Grave accent  \`     Left brace    \{     Vertical bar  \|
%%   Right brace   \}     Tilde         \~}
%%
%% TODO :  --> ajouter un systeme de commentaires
%%         --> ajouter des sections pour l'intro et la conclusion (commande {\Large \textbf{#1}} \vspace{.4cm})
%%         --> ajouter \graphicspath{{img/}{img_paper/}}
%%         --> regarder le package "import" pour l'ajouter et mieux classer le tout
%%

\NeedsTeXFormat{LaTeX2e}[1995/12/01]
\ProvidesClass{style/scienceUdeS}
 %             [2019/03/25 v2.1 document pour les documents de fin d'études du DI de l'UdeS Facult\'{e} des Sciences (par Benoit Fraikin (at) usherbrooke (dot) ca)]
 
 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
 %%
 %% Fins des des options de la classe et des flags
 %%
 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
 
 %%
 %% Options par défaut
 %% par défaut, cette classe est pour la maitrise en francais avec hypertexte
 %%
 
% \ExecuteOptions{letterpaper,12pt,twoside,onecolumn,final,openright,hypertexte,maitrise}
% 
% \ProcessOptions
 
%\newcommand\@ptsize{}
\newif\if@restonecol
\newif\if@titlepage \@titlepagetrue
\newif\if@openright
\newif\if@mainmatter \@mainmattertrue

\newif\if@cotutelle
\@cotutellefalse

\newcommand{\institutionPartenaire}[1]{}
\newcommand{\departementPartenaire}[1]{}
\newcommand{\gradePartenaire}[1]{}

\DeclareOption{cotutelle}{
	\@cotutelletrue
	\newcommand{\UdSInstitutionPartenaire}{}
	\newcommand{\UdSDepartementPartenaire}{}
	\newcommand{\UdSgradePartenaire}{}
	\renewcommand{\institutionPartenaire}[1]{\renewcommand{\UdSInstitutionPartenaire}{\uppercase{#1} }}
	\renewcommand{\departementPartenaire}[1]{\renewcommand{\UdSDepartementPartenaire}{#1}}	
	\renewcommand{\gradePartenaire}[1]{\renewcommand{\UdSgradePartenaire}{#1}}	
}


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%
%% Définition des options de la classe et des flags
%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%
%% Langue du document
%%

% par défaut, le document est en francais
\newif\if@english
\@englishfalse

\newcommand{\englishTitle}[1]{}
\newcommand{\UdSEngTitle}{}

\DeclareOption{francais}{
	\@englishfalse
	
} % option par défaut, pas besoin de la déclarer

\DeclareOption{english}{
	\@englishtrue
	\renewcommand{\englishTitle}[1]{\renewcommand{\UdSEngTitle}{\uppercase{#1} }}
}
%%
%% Format du document, marges, etc
%%

\newif\if@packageNatbib
\@packageNatbibtrue

\DeclareOption{nonatbib}{\@packageNatbibfalse}


\newif\if@hypertexte
\DeclareOption{hypertexte}{\@hypertextetrue}
\DeclareOption{sanshypertexte}{\@hypertextefalse}

\DeclareOption{bibliothequeNationale}{\@hypertextefalse \@twosidefalse \@mparswitchfalse}

%%
%% Codage des caractères
%%

\newif\if@charutf
\DeclareOption{caractereUtf}{\@charutftrue}
\DeclareOption{caractereLatin}{\@charutffalse}

%%
%% Logo de l'UdeS (ne fait rien pour le moment)
%%

\newif\if@logopdflatex
\DeclareOption{logopdflatex}{\@logopdflatextrue}
\DeclareOption{logolatex}{\@logopdflatexfalse}

%%
%% Types de documents possibles (par défaut, c'est le mémoire en info (M.Sc.) qui prime)
%%

\newif\if@these
\newif\if@essai
\newif\if@rapport
\newif\if@memoire

\@memoiretrue %par défaut

%\newif\if@maitriseGL

\DeclareOption{memoire}{%\@maitriseGLfalse 
	\@memoiretrue \@thesefalse \@essaifalse \@rapportfalse}

\DeclareOption{these}{\@thesetrue \@memoirefalse %\@maitriseGLfalse 
	\@essaifalse \@rapportfalse}
\DeclareOption{essai}{\@essaitrue \@thesefalse \@memoirefalse% \@maitriseGLfalse 
	\@rapportfalse}
\DeclareOption{rapport}{\@essaifalse \@thesefalse  \@memoirefalse%\@maitriseGLfalse 
	\@rapporttrue}
%\DeclareOption{maitriseGL}{\@maitriseGLtrue \@thesefalse \@essaifalse \@rapportfalse}

%%
%% Modifie la page de soumission ou acceptation du document
%%

\newif\if@enredaction
\@enredactionfalse
\DeclareOption{enRedaction}{\@enredactiontrue}

%%
%% Option de retrait  des listes non pertinentes
%%

\newif\if@listedesfigures
\@listedesfigurestrue
\DeclareOption{pasDeFigure}{\@listedesfiguresfalse}

\newif\if@listedestableaux
\@listedestableauxtrue
\DeclareOption{pasDeTableau}{\@listedestableauxfalse}

\newif\if@listedeslistings
\@listedeslistingstrue
\DeclareOption{pasDeCode}{\@listedeslistingsfalse}

\newif\if@listedesalgos
\@listedesalgostrue
\DeclareOption{pasDAlgo}{\@listedesalgosfalse}

\newif\if@listedesabrev
\@listedesabrevtrue
\DeclareOption{pasDAbreviation}{\@listedesabrevfalse}

%% Fallback
\DeclareOption*{
	\ClassWarning{scienceUdeS}{Unknown option '\CurrentOption'}
}

%\DeclareOption{10pt}{\OptionNotUsed}

\DeclareOption*{\PassOptionsToClass{\CurrentOption}{book}}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%
%% Fins des des options de la classe et des flags
%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%
%% Options par défaut
%% par défaut, cette classe est pour la maitrise en francais avec hypertexte
%%

\ExecuteOptions{hypertexte,maitrise}

%% options par défaut de la classe book qui peuvent être modifiées
\PassOptionsToClass{oneside, final, onecolumn}{book}

\ProcessOptions\relax

%% ces options de la classe book ne peuvent pas être modifiées
\LoadClass[letterpaper, 12pt]{book}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%
%% LANGUE DU DOCUMENT
%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\newcommand{\algorithmautorefname}{}

%% FRANCAIS

\newcommand{\modeFrancais}{
	\selectlanguage{french} % sert pour les césures de mots
	\renewcommand\contentsname{Table des matières}
	\renewcommand\listfigurename{Liste des figures}
	\renewcommand\listtablename{Liste des tableaux}
	\renewcommand{\lstlistlistingname}{Liste des codes sources}%
	\renewcommand{\lstlistingname}{Code source}
	\renewcommand{\listalgorithmname}{Liste des algorithmes}%
	\renewcommand{\abbreviationsname}{Liste des abréviations}
	\renewcommand\figurename{Figure}
	\renewcommand\tablename{Tableau}
	\renewcommand{\AMSautorefname}{\equationautorefname}
	\renewcommand{\Hfootnoteautorefname}{\footnoteautorefname}
	\renewcommand{\Itemautorefname}{\itemautorefname}
	\renewcommand{\itemautorefname}{élément}
	\renewcommand{\equationautorefname}{équation}
	\renewcommand{\footnoteautorefname}{note}
	\renewcommand{\figureautorefname}{figure}
	\renewcommand{\tableautorefname}{tableau}
	\renewcommand{\partautorefname}{partie}
	\renewcommand{\appendixautorefname}{annexe}
	\renewcommand{\chapterautorefname}{chapitre}
	\renewcommand{\sectionautorefname}{section}
	\renewcommand{\subsectionautorefname}{sous-section}
	\renewcommand{\subsubsectionautorefname}{sous-section}
	\renewcommand{\paragraphautorefname}{paragraphe}
	\renewcommand{\subparagraphautorefname}{sous-paragraphe}
	\renewcommand{\FancyVerbLineautorefname}{ligne}
	\renewcommand{\theoremautorefname}{théorème}
	\renewcommand{\algorithmautorefname}{algorithme}
	\floatname{algorithm}{Algorithme}
	%	\def\algautorefname{algorithme}
}

\newcommand{\frenchMode}{\modeFrancais}

%% ANGLAIS

\newcommand{\modeAnglais}{
	\selectlanguage{english}   % sert pour les césures de mots
	\renewcommand\contentsname{Table of Contents}
	\renewcommand\figurename{Figure}
	\renewcommand\tablename{Table}
	\renewcommand{\AMSautorefname}{\equationautorefname}
	\renewcommand{\Hfootnoteautorefname}{\footnoteautorefname}
	\renewcommand{\Itemautorefname}{\itemautorefname}
	\renewcommand{\listalgorithmname}{List of Algorithms}%
	\renewcommand{\lstlistlistingname}{List of Listings}%
	\renewcommand{\abbreviationsname}{List of Abbreviations}
	\renewcommand{\itemautorefname}{item}
	\renewcommand{\equationautorefname}{Equation}
	\renewcommand{\footnoteautorefname}{footnote}
	\renewcommand{\figureautorefname}{Figure}
	\renewcommand{\tableautorefname}{Table}
	\renewcommand{\partautorefname}{Part}
	\renewcommand{\appendixautorefname}{Appendix}
	\renewcommand{\chapterautorefname}{Chapter}
	\renewcommand{\sectionautorefname}{Section}
	\renewcommand{\subsectionautorefname}{Subsection}
	\renewcommand{\subsubsectionautorefname}{Subsubsection}
	\renewcommand{\paragraphautorefname}{Paragraph}
	\renewcommand{\subparagraphautorefname}{Subparagraph}
	\renewcommand{\FancyVerbLineautorefname}{line}
	\renewcommand{\theoremautorefname}{Theorem}
	\renewcommand{\lstlistingname}{Listing}
	\renewcommand{\algorithmautorefname}{algorithm}
	\floatname{algorithm}{Algorithm}
}

\newcommand{\englishMode}{
	\modeAnglais
}

%% MODE PAR DÉFAUT

\if@english
	\newcommand{\modeDefaut}{\modeAnglais}
\else
	\newcommand{\modeDefaut}{\modeFrancais}
\fi

\newcommand{\defaultMode}{\modeDefaut}

%% ===========================================
%% LES PAQUETS UTILES
%% ===========================================

%\if@charutf
%	\usepackage[utf8]{inputenc}
%\else
%	\usepackage[latin1]{inputenc}
%\fi

\usepackage[T1]{fontenc}
\usepackage{PTSansNarrow}
%\usepackage{cfr-lm}
%\usepackage{fix-cm}
\usepackage{anyfontsize}
\usepackage{lmodern} % Le dernier usepackage sur un font est appliqué.
\usepackage{enumerate}
% permet de faciliter les références croisées (les définitions sont dans le fichier refstyle.def)
% note : ajouter une option norefstyle pour éviter l'usage de ce package au besoin. Semble causer des problèmes dans overleaf avec texlive 2022. fonctionne avec d'autres versions
% note : Il faut inclure refstyle avant amsmath pour éviter des problèmes (https://tex.stackexchange.com/questions/672975/refstyle-is-conflicted-with-amsmath)
\usepackage[english, french]{refstyle}

\usepackage{amsfonts, amsmath, amssymb, stmaryrd, latexsym}
\usepackage{xspace, setspace}
\usepackage{array}
\usepackage[dvipsnames,usenames]{color}
\usepackage[table]{xcolor}
\usepackage{wrapfig, epsfig}
\if@packageNatbib
	\usepackage[numbers, sort]{natbib}
\fi

\usepackage[format=hang,labelfont={bf,sf}]{caption}
%\DeclareCaptionType[fileext=flt]{algorithm}

\usepackage[english, french]{babel}
%\usepackage[fixlanguage]{babelbib} %pas l'air à bien fonctionner

\usepackage{fancyhdr}
\usepackage{geometry}
\usepackage{ulem} % for sout and underline
\renewcommand{\emph}{\textit}

\usepackage[scaled=.90]{helvet}
\usepackage{courier}

\usepackage{listings} % (pour le code source  informatique)

%\usepackage{xcolor}
%New colors defined below
\definecolor{codegreen}{rgb}{0,0.6,0}
\definecolor{codegray}{rgb}{0.5,0.5,0.5}
\definecolor{codepurple}{rgb}{0.58,0,0.82}
\definecolor{codered}{rgb}{1,0,0}

\lstdefinestyle{DIUdeSStyle}{
	commentstyle=\color{codegray},
	keywordstyle=\bfseries\color{blue},
	numberstyle=\tiny\color{codegray},
	stringstyle=\color{codegreen},
	basicstyle=\ttfamily\small,
	identifierstyle=,
	frame=tb, %none/leftline/topline/bottomline/lines/single/shadowbox
	breakatwhitespace=false,         
	breaklines=true,                 
	captionpos=b,                    
	keepspaces=true,                 
	numbers=left,                    
	numbersep=5pt,                  
	showspaces=false,                
	showstringspaces=false,
	showtabs=false,                  
	tabsize=4,
	stepnumber=5, 
	firstnumber= 1,
}

\lstset{style=DIUdeSStyle, extendedchars=true, floatplacement=tb, breaklines=true,}


% Évite des problèmes avec les accents dans les fichiers de code inclus
\lstset{literate=
	{á}{{\'a}}1 {é}{{\'e}}1 {í}{{\'i}}1 {ó}{{\'o}}1 {ú}{{\'u}}1
	{Á}{{\'A}}1 {É}{{\'E}}1 {Í}{{\'I}}1 {Ó}{{\'O}}1 {Ú}{{\'U}}1
	{à}{{\`a}}1 {è}{{\`e}}1 {ì}{{\`i}}1 {ò}{{\`o}}1 {ù}{{\`u}}1
	{À}{{\`A}}1 {È}{{\'E}}1 {Ì}{{\`I}}1 {Ò}{{\`O}}1 {Ù}{{\`U}}1
	{ä}{{\"a}}1 {ë}{{\"e}}1 {ï}{{\"i}}1 {ö}{{\"o}}1 {ü}{{\"u}}1
	{Ä}{{\"A}}1 {Ë}{{\"E}}1 {Ï}{{\"I}}1 {Ö}{{\"O}}1 {Ü}{{\"U}}1
	{â}{{\^a}}1 {ê}{{\^e}}1 {î}{{\^i}}1 {ô}{{\^o}}1 {û}{{\^u}}1
	{Â}{{\^A}}1 {Ê}{{\^E}}1 {Î}{{\^I}}1 {Ô}{{\^O}}1 {Û}{{\^U}}1
	{ã}{{\~a}}1 {ẽ}{{\~e}}1 {ĩ}{{\~i}}1 {õ}{{\~o}}1 {ũ}{{\~u}}1
	{Ã}{{\~A}}1 {Ẽ}{{\~E}}1 {Ĩ}{{\~I}}1 {Õ}{{\~O}}1 {Ũ}{{\~U}}1
	{œ}{{\oe}}1 {Œ}{{\OE}}1 {æ}{{\ae}}1 {Æ}{{\AE}}1 {ß}{{\ss}}1
	{ű}{{\H{u}}}1 {Ű}{{\H{U}}}1 {ő}{{\H{o}}}1 {Ő}{{\H{O}}}1
	{ç}{{\c c}}1 {Ç}{{\c C}}1 {ø}{{\o}}1 {å}{{\r a}}1 {Å}{{\r A}}1
	{€}{{\euro}}1 {£}{{\pounds}}1 {«}{{\guillemotleft}}1
	{»}{{\guillemotright}}1 {ñ}{{\~n}}1 {Ñ}{{\~N}}1 {¿}{{?`}}1 {¡}{{!`}}1 
}
%\lstnewenvironment{code}{\lstset{numbers=left,numberstyle=\tiny,float}}{}

\if@hypertexte
  % Pour créer une version PDF contenant des liens colorés
  \usepackage[pdftex, colorlinks, linkcolor=blue, allcolors=blue]{hyperref}
\else
  \usepackage[ pdftex]{hyperref}
\fi

\usepackage{booktabs}
\usepackage{doi}

\usepackage[final]{pdfpages}
\usepackage[a-1b]{pdfx}

% LES DIMENSIONS DE LA PAGE
\setlength{\topmargin}{0pt}
\setlength{\footskip}{24pt}
\setlength{\headheight}{36pt}
\setlength{\headsep}{18pt}
\setlength{\textwidth}{6in}
\setlength{\textheight}{8in}
% -------------------------------------------------
\if@twoside
  \setlength{\oddsidemargin}{0.5in}
  \setlength{\evensidemargin}{0in}
\else
  \setlength{\oddsidemargin}{0.25in}
  \setlength{\evensidemargin}{0.25in}
\fi
% -------------------------------------------------

\if@logopdflatex
  \newcommand{\logoUDeS}{\includegraphics[width=2in]{logoudes.pdf}}
\else
  \newcommand{\logoUDeS}{\includegraphics[width=2in]{logoudes.eps}}
\fi

\newcommand{\email}{\texttt}
\newenvironment{changemargin}[2]{%
    \begin{list}{}{%
      \setlength{\topsep}{0pt}%
      \setlength{\leftmargin}{0pt}%
      \setlength{\rightmargin}{0pt}%
      \setlength{\listparindent}{\parindent}%
      \setlength{\itemindent}{\parindent}%
      \setlength{\parsep}{0pt plus 1pt}%
      \addtolength{\leftmargin}{#1}%
      \addtolength{\rightmargin}{#2}%
    } \item \noindent %\vspace*{-1.5em}
  }{%
    \end{list}%
  }%


% diverses sections du document (autre que chapitre régulier)
\newcommand{\UDSAuteurDocument}{}
\newcommand{\auteur}[1]{\renewcommand{\UDSAuteurDocument}{#1}}
\newcommand{\UDSTitreDocument}{}
\newcommand{\titre}[1]{\renewcommand{\UDSTitreDocument}{\uppercase{#1}}}

\newcommand{\UDSDate}{\today}
\newcommand{\ChangerDate}[1]{\renewcommand{\UDSDate}{{#1}}}

\newcommand{\UDSMotsCles}{}
\newcommand{\motsCles}[1]{\renewcommand{\UDSMotsCles}{\textbf{Mots-cl\'es}:~{#1}}}

\newcommand{\UDSKeywords}{}
\newcommand{\keywords}[1]{\renewcommand{\UDSKeywords}{\textbf{Keywords}:~{#1}}}

\newcommand{\UDSchapitreSpecial}[1]{%
    \chapter*{#1}
    \addcontentsline{toc}{chapter}{#1}
    \@mkboth{\normalfont\scshape #1}%
            {\normalfont\scshape #1}%
}

\newcommand{\Introduction}{
    \UDSchapitreSpecial{Introduction}
}
\newcommand{\Conclusion}{
    \UDSchapitreSpecial{Conclusion}
}

%=================== COMMANDES POUR LA PAGE DE JURY
\newcommand{\UDSDECISIONJURY}{}

\newcommand{\DecisionDuJury}[1]{%
	\renewcommand{\UDSDECISIONJURY}{
		\parbox[c][\textheight][s]{1\textwidth}{%0.85? la précédente valeur
			\begin{center}	
				\if@enredaction Le \displaydate{dateSoumission} \\[1cm]
					\if@these 
						Th\`ese déposée
					\else 
						M\'emoire déposé
					\fi
				 par \UDSAuteurDocument{} \\[1cm]
				\else Le \displaydate{dateAcceptation} \\[1cm]
					Le jury a accepté 
					\if@these
						la th\`ese
					\else
						le m\'emoire
					\fi
					de \UDSAuteurDocument{} dans sa version finale \\[1cm]
				\fi
			
				\textbf{Membres du jury} \\[.5cm]
				{#1}
				\vfill
			\end{center}
		}
	}%
}

% par défaut le département sera informatique et aucune institution ne sera affichée
\newcommand{\Directeur}[2][]{
	\MembreJury[#1]{Prof.}{#2}{Directeur}
}

\newcommand{\Directrice}[2][]{
	\MembreJury[#1]{Prof.}{#2}{Directrice}
}

\newcommand{\Codirecteur}[2][]{
	\MembreJury[#1]{Prof.}{#2}{Codirecteur}
}

\newcommand{\Codirectrice}[2][]{
	\MembreJury[#1]{Prof.}{#2}{Codirectrice}
}

\newcommand{\MembreInterne}[1]{
	\MembreJury{Prof.}{#1}{Membre interne}
}
%
\newcommand{\MembreExterne}[3][]{
	\MembreJury[#2]{#1}{#3}{Membre externe}
}

\newcommand{\President}[2][]{
	\MembreJury[#1]{Prof.}{#2}{Pr\'esident-rapporteur}
}

\newcommand{\Presidente}[2][]{
	\MembreJury[#1]{Prof.}{#2}{Pr\'esidente-rapporteuse}
}

%\newcommand{\Etudiant}[1]{Monsieur {#1}}
%\newcommand{\Etudiante}[1]{Madame {#1}}

\newcommand{\card}[1]{}

\newcommand{\MembreJury}[4][]%
% 1 : affiliation - par défaut, DI
% 2 : titre
% 3 : nom
% 4 : role dans le jury
{%
	{
		\normalsize
		#2						% titre
		#3,  					% nom
		\textit{#4}\\			%role
	}
	\footnotesize
		\ifthenelse{\equal{#1}{}}{Département d'informatique}{#1} 
		\\[3mm]
}
%		\@ifnotempty{#1}{, #1}\\

%===================

\newenvironment{chapeauArticle}[1]{
	\begin{changemargin}{4em}{4em}%
		\begin{center}
			{\bf #1}
		\end{center}%
} {
	\end{changemargin}
}

\newenvironment{contributions}{
	\begin{chapeauArticle}{\if@english
			Presentation of the article 
			\else
			Présentation de l'article
			\fi }
} {
	\end{chapeauArticle}
}


\newcommand{\titleArticle}[1]{%
	\begin{singlespacing}%
		\begin{center}
			{\Large\bf #1}
		\end{center}%
	\end{singlespacing}%
}


\newenvironment{authorsArticle}{
	\begin{chapeauArticle}{
		\if@english
			Authors
		\else
			Auteur.e.s
		\fi 
	}} 
{
	\end{chapeauArticle}
}

\newenvironment{abstractArticle}{
	\begin{chapeauArticle}{
			\if@english
				Abstract
			\else
				Résumé
			\fi
	}
} {
	\end{chapeauArticle}
}

\newenvironment{commentairesArticle}{
	\begin{chapeauArticle}{\if@english
			Contribution of the authors
			\else
			Contributions des auteur.e.s
			\fi 
			}
} {
	\end{chapeauArticle}
}

\newcommand{\keywordsArticle}[1]{
	\begin{changemargin}{2em}{2em}%
		\textbf{Keywords}: {#1}%
     \end{changemargin}
}
                             
%%
%% MISE EN PAGE
%%

\fancyhf{} % efface le contenu de fancy header par d\'efaut
\renewcommand{\headrulewidth}{0 pt} % taille de la ligne de haut de page
%\renewcommand{\footrulewidth}{0 pt} % taille de la ligne de bas de page

\lhead[\fancyplain{}{}]                 {\fancyplain{}{\rightmark}}
\chead[\fancyplain{}{}]                 {\fancyplain{}{}}
\rhead[\fancyplain{}{\leftmark}]        {\fancyplain{}{}}
% Traitement en fonction de l'option twoside
% -------------------------------------------------
\if@twoside
  % recto-verso
  \lfoot[\fancyplain{\thepage}{\thepage}] {\fancyplain{}{}}
  \cfoot[\fancyplain{}{}]                 {\fancyplain{}{}}
  \rfoot[\fancyplain{}{}]                 {\fancyplain{\thepage}{\thepage}}
\else
  % simple face
  \lfoot[\fancyplain{}{}]                 {\fancyplain{}{}}
  \cfoot[\fancyplain{\thepage}{\thepage}] {\fancyplain{\thepage}{\thepage}}
  \rfoot[\fancyplain{}{}]                 {\fancyplain{}{}}
\fi
% -------------------------------------------------

\newcommand{\UDSSommaire}{}
\newcommand{\sommaire}[1]{\renewcommand{\UDSSommaire}{
	\UDSchapitreSpecial{Sommaire}%
    {#1}\\[2ex]%
    \UDSMotsCles{}%
    }%
}

\newcommand{\UDSAbstract}{}
\newcommand{\abstract}[1]{\renewcommand{\UDSAbstract}{
		\UDSchapitreSpecial{Abstract}%
		{#1}\\[2ex]%
		\UDSKeywords{}%
	}%
}

\newcommand{\UDSRemerciements}{}
\newcommand{\remerciements}[1]{\renewcommand{\UDSRemerciements}{
		\if@english
			\UDSchapitreSpecial{Acknowledgements}{#1}
		\else
			\UDSchapitreSpecial{Remerciements}{#1}
    	\fi
    }%
}
\newcommand{\UDSAbreviations}{}
\newcommand{\abreviations}[1]{
	\renewcommand{\UDSAbreviations}{
%		\UDSchapitreSpecial{Liste des abréviations}%
    		\printglossary[type=abbreviations]
    }%
}


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%
%% ENTETE DU DOCUMENT
%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


\newcommand{\enteteDuDocument}{
	% page titre
	
	\modeFrancais  % la page titre est toujours en francais
	
    \frontmatter
	\pagestyle{empty} % Empêcher l'apparition d'un numéro de page sur la page de présentation
   %
	%
	\onehalfspacing%
	\maketitle%

\addtocounter{page}{1}

	\if@these
		\UDSDECISIONJURY{}%
	\fi
	 
	\if@memoire
		\UDSDECISIONJURY{}%
	\fi
	
	%
	\pagestyle{fancyplain}%

	%
	% SOMMAIRE
	\UDSSommaire{}%
	
	 \if@english
	 	\modeAnglais
		\UDSAbstract{}%
	\fi	 
	% FIN DU SOMMAIRE
	
	
%% REMERCIEMENTS 
	\UDSRemerciements%

%% TABLE DES MATIÈRES
	\setcounter{tocdepth}{2} %Ne pas mettre plus de 2 de profond
	\clearpage
	\phantomsection
	\addcontentsline{toc}{chapter}{\contentsname}
	\tableofcontents%

%% Liste des figures
	\if@listedesfigures
		\clearpage
		\phantomsection
		\addcontentsline{toc}{chapter}{\listfigurename}
		\listoffigures%
	\fi

%% Liste des tableaux
	\if@listedestableaux
		\clearpage
		\phantomsection
		\addcontentsline{toc}{chapter}{\listtablename}
		\listoftables
	\fi
	
%% Liste des algorithmes (en pseudocode)
	\if@listedesalgos
		\clearpage
		\phantomsection
		\addcontentsline{toc}{chapter}{\listalgorithmname}
		\listofalgorithms
	\fi

%% Liste des codes source
	\if@listedeslistings
		\clearpage
		\phantomsection
		\addcontentsline{toc}{chapter}{\lstlistlistingname}
		\lstlistoflistings
	\fi

	% Listes des abbréviations
	\if@listedesabrev
		\printglossary[type=abbreviations]
	\fi

	%
	\mainmatter
}
%

\title{  
	\vspace*{-5ex}
	\begin{spacing}{1}
    	\bf\large \UDSTitreDocument%{} 
    \end{spacing} \vspace*{5ex}
    
    	\begin{spacing}{1}
    		\bf \small  \UdSEngTitle
    	\end{spacing} \vspace*{-2ex} % vide si en francais.
 %   \fi
	  \vspace*{4ex}
}

\author{ 
	par\\[3ex]
    \UDSAuteurDocument{}\vspace*{6ex}
}

%% type du document, grade, lieu et date est dans la commande date (weird)

\date{   
	% Type de document
%	\begin{spacing}{1}
	\if@these
         Th\`ese \if@cotutelle
         en cotutelle \fi pr\'esent\'ee \if@cotutelle \\[4ex] \fi au D\'epartement d'informatique
    \else \if@essai
         Essai présenté au Département d'informatique
    \else \if@rapport
         Rapport de stage présenté au Département d'informatique
	\else         
         Mémoire présenté au Département d'informatique
    \fi \fi \fi	\\%
	%        
    % Grade à obtenir pourra être adapté pour les grades de la maitrise G.L. pour prof. en exercice au besoin (pas le cas pour le moment)
    \if@these
         en vue de l'obtention du grade de philosophi\ae{} doctor (Ph.D.)%\\[8ex]
    \else
    	en vue de l'obtention du grade de ma\^itre \`es sciences (M.Sc.)%\\[8ex]
    \fi
%    \end{spacing}
%	%    
    \if@cotutelle
    	\\
    \else
	    \\[8ex]
    \fi
%	%    
     FACULT\'E DES SCIENCES\if@cotutelle, \else \\ \fi
     UNIVERSIT\'E DE SHERBROOKE 
     \if@cotutelle
    	\\[5ex]\UdSDepartementPartenaire \\ en vue de l'obtention du grade de \UdSgradePartenaire \\ \UdSInstitutionPartenaire
     \fi
     \\[8ex] Sherbrooke, Qu\'ebec, Canada, \UDSDate
}

\endinput

%%
%% End of file `scienceUdeS.cls'.
