package outils;

import java.awt.FlowLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ExempleLatex {

    public static void main(String[] args) {

        String TEMP_DIRECTORY = "temp";
        String TEMP_TEX_FILE_NAME = "New22"; // for New22.tex

        // 1. Prepare the .tex file
        String newLineWithSeparation = System.getProperty("line.separator")+System.getProperty("line.separator");
        String math = "";
        math += "\\documentclass[border=0.50001bp,convert={convertexe={imgconvert},outext=.png}]{standalone}" + newLineWithSeparation;
        math += "\\usepackage{amsfonts}" + newLineWithSeparation;
        math += "\\usepackage{amsmath}" + newLineWithSeparation;
        math += "\\begin{document}" + newLineWithSeparation;
        math += "$\\sqrt 3$" + newLineWithSeparation;
        math += "\\end{document}";

        // 2. Create the .tex file
        FileWriter writer = null;
        try {
            writer = new FileWriter(TEMP_DIRECTORY + "/" + TEMP_TEX_FILE_NAME + ".tex", false);
            writer.write(math, 0, math.length());
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // 3. Execute LaTeX from command line  to generate pdf
        ProcessBuilder pb = new ProcessBuilder("pdflatex", "-shell-escape", TEMP_TEX_FILE_NAME + ".tex");
        pb.directory(new File(TEMP_DIRECTORY));
        try {
            Process p = pb.start();
            StreamPrinter fluxSortie = new StreamPrinter(p.getInputStream(), false);
            StreamPrinter fluxErreur = new StreamPrinter(p.getErrorStream(), false);
            new Thread(fluxSortie).start();
            new Thread(fluxErreur).start();
            p.waitFor();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

        // 4. Convert pdf to png
        Pdf2Image.main(new String[] {TEMP_DIRECTORY + "/" +TEMP_TEX_FILE_NAME+".pdf",TEMP_DIRECTORY + "/" +TEMP_TEX_FILE_NAME});
        
        // 5. Display picture
        JFrame maFrame = new JFrame();
        maFrame.setResizable(false);
        maFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        maFrame.setSize(400, 400);
        maFrame.getContentPane().setLayout(new FlowLayout());
        maFrame.getContentPane().add(new JLabel(new ImageIcon(TEMP_DIRECTORY + "/" + TEMP_TEX_FILE_NAME + ".png")));
        maFrame.pack();
        maFrame.setVisible(true);
        
        
        // 6. Delete files
        for (File file : (new File(TEMP_DIRECTORY).listFiles())) {
            if (file.getName().startsWith(TEMP_TEX_FILE_NAME + ".")) {
                file.delete();
            }
        }
    }
}
