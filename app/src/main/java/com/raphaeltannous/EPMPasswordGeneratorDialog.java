package com.raphaeltannous;

import java.awt.Color;
import java.awt.Container;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.NumberFormatter;

import com.formdev.flatlaf.FlatClientProperties;

import net.miginfocom.swing.MigLayout;

/**
 * EPMPasswordGenerator
 */
public class EPMPasswordGeneratorDialog extends JDialog {
    private JPanel dialogPane;
    private JPanel contentPanel;

    private JPasswordField remotePasswordField;
    private JPasswordField localPasswordField;

    private JButton okButton;
    private JButton cancelButton;
    private JButton generateButton;

    private JCheckBox useLowerCheckBox;
    private JCheckBox useUpperCheckBox;
    private JCheckBox useDigitsCheckBox;
    private JCheckBox usePunctuationCheckBox;

    private JSpinner passwordLengthSpinner;
    private JSpinner numberOfWhiteSpacesSpinner;

    private boolean generateButtonActionListenerInProgress = false;

    private Color[] success = new Color[]{new Color(0, 255, 0), new Color(200, 255, 200)};

    EPMPasswordGeneratorDialog(
        Window owner,
        JPasswordField remotePasswordField
    ) {
        super(owner);

        this.remotePasswordField = remotePasswordField;

        initDialogComponents();

        getRootPane().setDefaultButton(okButton);

        // By default show the password
        localPasswordField.setEchoChar((char)0);

        // TODO: Add a button to copy the password in the passwordField

        // Show reveal password button
        localPasswordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true");

        // Generate a password by default
        generateButtonActionListener();
    }

    private void initDialogComponents() {
        dialogPane = new JPanel();
        contentPanel = new JPanel();

        JLabel localPasswordLabel = new JLabel();
        JLabel passwordLengthSpinnerLabel = new JLabel();
        JLabel numberOfWhiteSpacesSpinnerLabel = new JLabel();

        localPasswordField = new JPasswordField();

        useLowerCheckBox = new JCheckBox();
        useUpperCheckBox = new JCheckBox();
        useDigitsCheckBox = new JCheckBox();
        usePunctuationCheckBox = new JCheckBox();

        SpinnerNumberModel passwordLengthSpinnerModel = new SpinnerNumberModel(32, 1, 128, 1);
        passwordLengthSpinner = new JSpinner(passwordLengthSpinnerModel);
        passwordLengthSpinner.addChangeListener(e -> passwordLengthSpinnerChangeListener());

        numberOfWhiteSpacesSpinner = new JSpinner();
        numberOfWhiteSpacesSpinner.addChangeListener(e -> numberOfWhiteSpacesSpinnerChangeListener());

        // Update spinner model for numberOfWhiteSpacesSpinner
        updateNumberOfWhiteSpacesSpinnerModel();

        // Making the passwordLengthSpinner un-editable for invalid input.
        JFormattedTextField passwordLengthSpinnerText = ((JSpinner.NumberEditor) passwordLengthSpinner.getEditor()).getTextField();
        ((NumberFormatter) passwordLengthSpinnerText.getFormatter()).setAllowsInvalid(false);

        // Making the numberOfWhiteSpacesSpinner un-editable for invalid input.
        JFormattedTextField numberOfWhiteSpacesSpinnerText = ((JSpinner.NumberEditor) numberOfWhiteSpacesSpinner.getEditor()).getTextField();
        ((NumberFormatter) numberOfWhiteSpacesSpinnerText.getFormatter()).setAllowsInvalid(false);

        okButton = new JButton();
        cancelButton = new JButton();
        generateButton = new JButton();

        // this
        setTitle("Password Generator");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(777, 333);
        setModal(true);

        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout("insets 0, fill"));

        // dialogPane
        {
            dialogPane.setLayout(new MigLayout("insets 0, fill"));

            // contentPanel
            {
                contentPanel.setLayout(new MigLayout(
                    "insets 0, fill",
                    // Column Constraints
                    "[177!][177!]",
                    // Row Constraints
                    ""
                ));

                // localPasswordLabel
                localPasswordLabel.setText("Generated Password");
                localPasswordLabel.setLabelFor(localPasswordField);
                contentPanel.add(localPasswordLabel, "cell 0 0");

                // localPasswordField
                EPMUtilities.addChangeListener(localPasswordField, e -> localPasswordFieldActionListener());
                contentPanel.add(localPasswordField, "cell 0 1 2 1, growx");

                // useLowerCheckBox
                useLowerCheckBox.setText("Lowercase");
                useLowerCheckBox.setDisplayedMnemonicIndex(0);
                useLowerCheckBox.setMnemonic('L');
                useLowerCheckBox.setSelected(true);
                contentPanel.add(useLowerCheckBox, "cell 0 2, align left");

                // useUpperCheckBox
                useUpperCheckBox.setText("Uppercase");
                useUpperCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
                useUpperCheckBox.setDisplayedMnemonicIndex(0);
                useUpperCheckBox.setMnemonic('U');
                useUpperCheckBox.setSelected(true);
                contentPanel.add(useUpperCheckBox, "cell 1 2, align right");

                // useDigitsCheckBox
                useDigitsCheckBox.setText("Digits");
                useDigitsCheckBox.setDisplayedMnemonicIndex(0);
                useDigitsCheckBox.setMnemonic('D');
                useDigitsCheckBox.setSelected(true);
                contentPanel.add(useDigitsCheckBox, "cell 0 3, align left");

                // usePunctuationCheckBox
                usePunctuationCheckBox.setText("Punctuation");
                usePunctuationCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
                usePunctuationCheckBox.setDisplayedMnemonicIndex(0);
                usePunctuationCheckBox.setMnemonic('P');
                usePunctuationCheckBox.setSelected(true);
                contentPanel.add(usePunctuationCheckBox, "cell 1 3, align right");

                // passwordLengthSpinnerLabel
                passwordLengthSpinnerLabel.setText("Length");
                passwordLengthSpinnerLabel.setDisplayedMnemonicIndex(0);
                passwordLengthSpinnerLabel.setDisplayedMnemonic('S');
                passwordLengthSpinnerLabel.setLabelFor(passwordLengthSpinner);
                contentPanel.add(passwordLengthSpinnerLabel, "cell 0 4, align center");

                // numberOfWhiteSpacesSpinnerLabel
                numberOfWhiteSpacesSpinnerLabel.setText("Number of Spaces");
                numberOfWhiteSpacesSpinnerLabel.setDisplayedMnemonicIndex(0);
                numberOfWhiteSpacesSpinnerLabel.setDisplayedMnemonic('N');
                numberOfWhiteSpacesSpinnerLabel.setLabelFor(numberOfWhiteSpacesSpinner);
                contentPanel.add(numberOfWhiteSpacesSpinnerLabel, "cell 1 4, align center");

                // passwordLengthSpinner
                contentPanel.add(passwordLengthSpinner, "cell 0 5, align center");

                // numberOfWhiteSpacesSpinner
                contentPanel.add(numberOfWhiteSpacesSpinner, "cell 1 5, align center");

                // generateButton
                generateButton.setText("Generate");
                generateButton.setMnemonic('G');
                generateButton.setDisplayedMnemonicIndex(0);
                generateButton.addActionListener(e -> generateButtonActionListener());
                contentPanel.add(generateButton, "cell 0 6 2 1, align center");
            }
        }
        dialogPane.add(contentPanel, "align center");

        contentPane.add(dialogPane, "align center");
        setLocationRelativeTo(getOwner());
    }

    private void numberOfWhiteSpacesSpinnerChangeListener() {
        generateButtonActionListener();
    }

    private void generateButtonActionListener() {
        if (generateButtonActionListenerInProgress) {
            return;
        }

        generateButtonActionListenerInProgress = true;


        localPasswordField.setText(
            SecurePasswordGenerator.generatePassword(
                (int) passwordLengthSpinner.getValue(),
                (int) numberOfWhiteSpacesSpinner.getValue(),
                useLowerCheckBox.isSelected(),
                useUpperCheckBox.isSelected(),
                useDigitsCheckBox.isSelected(),
                usePunctuationCheckBox.isSelected()
            )
        );

        SwingUtilities.invokeLater(() -> generateButtonActionListenerInProgress = false);
    }

    private void updateNumberOfWhiteSpacesSpinnerModel() {
        int maxNumberOfWhiteSpaces = SecurePasswordGenerator.maxNumberOfWhitesSpaces(
            (int) passwordLengthSpinner.getValue()
        );

        SpinnerNumberModel newSpinnerModel = new SpinnerNumberModel(0, 0, maxNumberOfWhiteSpaces, 1);

        numberOfWhiteSpacesSpinner.setModel(newSpinnerModel);
    }

    private void passwordLengthSpinnerChangeListener() {
        generateButtonActionListener();
        updateNumberOfWhiteSpacesSpinnerModel();
    }

    private void localPasswordFieldActionListener() {
        boolean isEmpty = String.valueOf(localPasswordField.getPassword()).isEmpty();

        if (isEmpty) {
            localPasswordField.putClientProperty("JComponent.outline", "error");
        } else {
            localPasswordField.putClientProperty("JComponent.outline", this.success);
        }

        checkOkButtonStatus();
    }

    private void checkOkButtonStatus() {
        boolean  isLocalPasswordFieldEmpty = String.valueOf(localPasswordField.getPassword())   .isEmpty();

        if (isLocalPasswordFieldEmpty) {
            okButton.setEnabled(true);
            return;
        }

        okButton.setEnabled(false);
    }
}
