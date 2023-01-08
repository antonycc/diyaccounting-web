// JavaScript helper functions for gb-web

<!-- Use <script id="my_id" type="x-handlebars-template" (not x-tmpl-mustache so IntelliJ parses as a template) -->

// Handlebars configuration
Handlebars.registerHelper("when", (operand_1, operator, operand_2, options) => {
   let operators = {                     //  {{#when <operand1> 'eq' <operand2>}}
      'eq': (l,r) => l == r,              //  {{/when}}
      'noteq': (l,r) => l != r,
      'gt': (l,r) => (+l) > (+r),                        // {{#when var1 'eq' var2}}
      'gteq': (l,r) => ((+l) > (+r)) || (l == r),        //               eq
      'lt': (l,r) => (+l) < (+r),                        // {{else when var1 'gt' var2}}
      'lteq': (l,r) => ((+l) < (+r)) || (l == r),        //               gt
      'or': (l,r) => l || r,                             // {{else}}
      'and': (l,r) => l && r,                            //               lt
      '%': (l,r) => (l % r) === 0                        // {{/when}}
   }
   let result = operators[operator](operand_1,operand_2);
   if(result) return options.fn(this);
   return options.inverse(this);
});

Handlebars.registerHelper('replace', function( find, replace, options) {
   var string = options.fn(this);
   return string.replace( find, replace );
});

// Declare global application namespace
let gbWebApp = {};
if(!gbWebApp){
   console.log("Creating global application namespace: gbWebApp");
   gbWebApp = {};
}

gbWebApp.applySourceTemplateTo = function(source, context) {
   //console.log("Source: [[" + source + "]]");
   const template = Handlebars.compile(source);
   const markup = template(context);
   //console.log("Generated: [[" + markup + "]]");
   return markup;
}

// The template should resolve into a replacement HTML element (or elements).
gbWebApp.applyElementTemplateTo = function(element, context) {
   //const source = "<div id=\"aboutContentJstHardcoded\"></div>";
   const source = element.text();
   const markup = gbWebApp.applySourceTemplateTo(source, context);
   //element.empty().appendTo("<div>" + markup + "</div>");
   //element.text(markup); // - leaves the script wrapper
   //element.append(markup); // - doesn't replace the template
   //element.appendTo(markup); // - removes the script section but doesn't add the populated template
   //element.replaceAll(markup); //  as above
   //element.html(markup); // - leaves the script wrapper
   element.replaceWith(markup);
}

gbWebApp.applyAttributeTemplateTo = function(element, attribute, context) {
   const source = element.attr(attribute);
   const markup = gbWebApp.applySourceTemplateTo(source, context);
   element.attr(attribute, markup);
}

gbWebApp.applyTextBodyTemplateTo = function(element, context) {
   const source = element.text();
   const markup = gbWebApp.applySourceTemplateTo(source, context);
   element.text(markup);
}

gbWebApp.applyTemplateForElementsThatEndWith = function(templateEndsWith, context) {
   $('[id$='+templateEndsWith+']').each(function() {
      //const id = '#' + this.id;
      //const type = $(this).prop('nodeName');
      const element = $(this)
      //if( $(this).is("SCRIPT") ){
      if( element.is("SCRIPT") ){
         //console.log('Applying element template to ' + id + ' which is a ' + type);
         //gbWebApp.applyElementTemplateTo($(this), context);
         gbWebApp.applyElementTemplateTo(element, context);
      }else{
         //console.log('Applying text template to ' + id + ' which is a ' + type);
         //gbWebApp.applyTextBodyTemplateTo($(this), context);
         gbWebApp.applyTextBodyTemplateTo(element, context);
      }
   });
}

gbWebApp.newGuid = function() {
   return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,
         function(c) {
            var r = Math.random() * 16 | 0,
                  v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
         }).toUpperCase();
}
