Fork of tongue-tied (http://code.google.com/p/tongue-tied/) based on 3rd party modification: https://groups.google.com/forum/#!topic/tonguetied-users/h2RHHEsnOso

Original message:

Here are the Release Notes (based of tonguetied 1.3.6) :
* Maven 2 Integration : the source code was reorganized to match with
maven project structure. It was also splitted in various sub-projects
(web, core, ...)
* Add "NLS" export type : A new export file type was created : NLS.
The export is the same as for Java properties files but without using
UTF-8 characters (\uXXXX) with "Merge Globals" ability
* Multiple TransalationStates selection : now it is possible to select
more than one translation state for the export. All corresponding
placeholders are merge in the destination file(s)
* Refactored the export by introducing the Exporter interface and
ExporterFactory. ExportDataPostProcessor does not exist anymore, data
processing is implemented in specific Exporters
* Importers and exporters are configured as Spring beans
* Freemarker is now used directly instead of FMPP. This allows to load
the Freemarker templates from the classpath (FMPP only allowed to load
from file system)
* Placeholder generation : a new checkbox "Generate placeholders" was
added in the export GUI. If activated, this generate missing
translations from the DEFAULT language using destination language
prefix and the DEFAULT language translation.
Ex.
bundle.properties : key=Text auf Deutsch
bundle_fr.properties : key=fr_Text auf Deutsch
* Export maven plugin : A Maven 2 export plugin was created to link
translation files creation while building other project. This permit
to update the translations automatically when building your project. 

The complete code is available here :
https://filepaste.puzzle.ch/downloads/bfhAWdUFK5hQh7cW/tonguetied.zip

Developpers :
Jean-Philippe Menoud (jean-phili...@elca.ch)
Dominic Brügger (brue...@puzzle.ch)

