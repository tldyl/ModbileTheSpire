Hello Slay the Spire translators! This is a helpful guide to try and aid your translation efforts.

---------------------
----- SECTION 1 -----
---------------------

1. As the files to translate are all JSON documents, fields do not need to be translated. In fact, the game won't run if the fields are renamed.
EXAMPLE: Only the fields under "NAMES" and "TEXT" needs to be translated. (Marked with <- This!)

-------------------
"Ironclad": {
    "NAMES": [
        "The Ironclad" <- This!
    ], 
"TEXT": [
        "The remaining soldier of the Ironclads. NL Sold his soul to harness demonic energies." <- This!
    ]
},
--------------------

2. Formatting Quirks:
    -Text is colored to highlight keywords and other important concepts. Colors are as follows: #y, #r, #g, #b, #p. These colors are Gold, Red, Green, Blue, Purple (Only in events)
    EXAMPLE: "#yI'm #yGolden!"

    -For events and dialogs, if a word is surrounded by ~ or @, they are wavy or shaky in game. 
    EXAMPLE: "@This@ @is@ @shaky.@" "~This~ ~is~ ~wavy.~""

    -Colors and text effects can be combined. However, the color must always come before the effect.
    EXAMPLE: "#y@CLANG@ #y@CLANG@"

    -Cards utilize dynamic values. These are !D!, !B!, !M!. They stand for Damage, Block, and Misc.
    EXAMPLE: "Deal !D! damage."
    EXAMPLE: "Gain !B! Block."
    EXAMPLE2: "Draw !M! cards."

    -Keywords found in keywords.json must be capitalized when they are shown on a card, power, or relic description.
    EXAMPLE: "Apply 2 Vulnerable."

    -NL stands for new line. New lines are added to make the text feel easier to read or separates two distinct sentences or quotes. There must be a space on either side for the new line to be appended.
    EXAMPLE: "Hello. NL I am Bob."

    -Sometimes descriptions will be disjointed. This is because we need to insert dynamic values into some text.
    EXAMPLE:
    "I ate #b",
    " pies."
    Would look like: "I ate #b6 pies." in the game
	
3. Lore Quirks
    The animal references in Slay the Spire are misspelled because the animals aren't the same as the ones in our world, Earth- as the game doesn't take place anywhere near our time and place. Frog -> Phrog. Crane -> Krane. Bird -> Byrd are notable examples in the game.

4. If you have any questions, please reach out to us in the #localization channel on Discord. Don't forget to collaborate with others that are working on your language.

---------------------
----- SECTION 2 -----
---------------------

How to test localization for Slay the Spire:

1. Create a backup of desktop-1.0.jar (copy/paste -> desktop-1.0.backup)
2. Locate and extract the desktop-1.0.jar file (just like a ZIP file! We recommend using 7z)
3. Choose and modify the language you wish to translate (Example: localization/eng/ui.json)
4. Rename desktop-1.0.jar to desktop-1.0.zip
5. Add your edited content into the zip, replacing the original contents.
6. Rename desktop-1.0.zip to desktop-1.0.jar
7. Run the game through SlayTheSpire.exe
8. View your changes in-game! Wow!

---------------------
----- SECTION 3 -----
---------------------

Misc Notes:

1. SRB is Serbian (Latin) and SRP is Serbian (Cyrillic)

Thanks,
Anthony and Casey