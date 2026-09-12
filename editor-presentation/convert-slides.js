// Load Asciidoctor.js and the reveal.js converter
var asciidoctor = require('@asciidoctor/core')()
var asciidoctorRevealjs = require('@asciidoctor/reveal.js')


asciidoctorRevealjs.register()

// Convert the document 'presentation.adoc' using the reveal.js converter
var options = {safe: 'safe', backend: 'revealjs', attributes: {
    'plantuml-server-url': 'http://localhost:8080'
  }};
asciidoctor.convertFile('presentation.adoc', options); 