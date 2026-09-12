//npm install chokidar@3

const asciidoctor = require('@asciidoctor/core')();
const asciidoctorRevealjs = require('@asciidoctor/reveal.js');
const browserSync = require('browser-sync').create();
const chokidar = require('chokidar');

asciidoctorRevealjs.register();

function compile() {
  console.log('Compiling AsciiDoc...');
  asciidoctor.convertFile('presentation.adoc', { safe: 'safe', backend: 'revealjs' });
  browserSync.reload();
}

// Start local server with live reload
browserSync.init({
  server: "./",
  index: "presentation.html"
});

// Watch for changes in your .adoc files
chokidar.watch('*.adoc').on('change', compile);

// Initial compilation
compile();