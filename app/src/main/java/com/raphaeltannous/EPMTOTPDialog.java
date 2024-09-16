package com.raphaeltannous;

import java.awt.Container;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.formdev.flatlaf.FlatClientProperties;

import net.miginfocom.swing.MigLayout;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.TOTPGenerator;

/**
 * EPMTOTPDialog
 */
public class EPMTOTPDialog extends JDialog {
    private EPMPasswordsPanel passwordsPanel;
    private int passwordId;

    private JPanel dialogPane;
    private JPanel contentPanel;

    private JLabel totpLabel;

    private JPasswordField totpPasswordField;

    private JButton okButton;
    private JButton cancelButton;
    private JButton updateTOTPButton;

    private tOTPTimer localtOTPTimer;
    private boolean isLocaltOTPTimerRunning = false;

    private Timer timer;

    EPMTOTPDialog(
        Window owner,
        EPMPasswordsPanel passwordsPanel,
        int passwordId
    ) {
        super(owner);

        this.passwordsPanel = passwordsPanel;
        this.passwordId = passwordId;

        initDialogComponents();

        getRootPane().setDefaultButton(okButton);

        // Show revel password button for toptPasswordField
        totpPasswordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true");

        String otpKey = this.passwordsPanel.db.fetchOTP(this.passwordId);

        if (!otpKey.isEmpty()) {
            localtOTPTimer = new tOTPTimer(
                totpLabel,
                otpKey
            );

            this.timer = new Timer();
            this.timer.schedule(localtOTPTimer, 0, 1000);

            isLocaltOTPTimerRunning = true;

            totpPasswordField.setText(otpKey);
        }
    }

    public void initDialogComponents() {
        dialogPane = new JPanel();
        contentPanel = new JPanel();

        totpLabel = new JLabel();

        totpPasswordField = new JPasswordField();

        okButton = new JButton();
        cancelButton = new JButton();
        updateTOTPButton = new JButton();

        // this
        setTitle("TOTP");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancelButtonActionListener();
            }
        });
        setSize(222, 222);
        setResizable(false);
        setModal(true);

        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout("insets 0, fill"));

        // dialogPane
        {
            dialogPane.setLayout(new MigLayout("insets 0"));

            // contentPanel
            {
                contentPanel.setLayout(new MigLayout("insets 0"));

                // toptNowLabel
                totpLabel.putClientProperty("FlatLaf.styleClass", "h00");
                contentPanel.add(totpLabel, "cell 0 0 2 1, align center");

                // totpPasswordField
                totpPasswordField.setColumns(10);
                contentPanel.add(totpPasswordField, "cell 0 2 2 1, growx");

                // updateTOTPButton
                updateTOTPButton.setText("Update");
                updateTOTPButton.setMnemonic('U');
                updateTOTPButton.setDisplayedMnemonicIndex(0);
                updateTOTPButton.addActionListener(e -> updateTOTPButtonActionListener());
                contentPanel.add(updateTOTPButton, "cell 0 3 2 1, align center");

                // okButton
                okButton.setText("Ok");
                okButton.setMnemonic('O');
                okButton.setDisplayedMnemonicIndex(0);
                okButton.addActionListener(e -> okButtonActionListener());
                contentPanel.add(okButton, "cell 0 4, align center");

                // cancelButton
                cancelButton.setText("Cancel");
                cancelButton.setMnemonic('C');
                cancelButton.setDisplayedMnemonicIndex(0);
                cancelButton.addActionListener(e -> cancelButtonActionListener());
                contentPanel.add(cancelButton, "cell 1 4, align center");

            }
        }
        dialogPane.add(contentPanel, "align center");

        contentPane.add(dialogPane, "align center");
        setLocationRelativeTo(getOwner());
    }

    private void cancelButtonActionListener() {
        if (
            Objects.nonNull(this.localtOTPTimer) &&
            Objects.nonNull(this.timer)
        ) {
            this.localtOTPTimer.cancel();

            this.timer.cancel();
            this.timer.purge();
        }

        dispose();
    }

    private void okButtonActionListener() {
        cancelButtonActionListener();
    }

    private void updateTOTPButtonActionListener() {
        String otpKey = String.valueOf(totpPasswordField.getPassword());
        String storedOTPKey = passwordsPanel.db.fetchOTP(this.passwordId);

        if (otpKey.equals(storedOTPKey)) {
            return;
        }

        passwordsPanel.db.modifyOTP(this.passwordId, otpKey);

        if (!isLocaltOTPTimerRunning && !otpKey.isEmpty()) {
            this.localtOTPTimer = new tOTPTimer(
                totpLabel,
                otpKey
            );

            localtOTPTimer.totpKey = otpKey;

            this.timer = new Timer();
            this.timer.schedule(this.localtOTPTimer, 0, 1000);

            isLocaltOTPTimerRunning = true;
        }

        if (
            otpKey.isEmpty() &&
            Objects.nonNull(this.localtOTPTimer) &&
            Objects.nonNull(this.timer)
        ) {
            totpLabel.setText("");

            this.localtOTPTimer.cancel();
            this.timer.cancel();

            isLocaltOTPTimerRunning = false;
        }
    }
}

class tOTPTimer extends TimerTask {
    private JLabel totpLabel;
    protected String totpKey;

    tOTPTimer(
        JLabel totpLabel,
        String totpKey
    ) {
        this.totpLabel = totpLabel;
        this.totpKey = totpKey;
    }

    private String getTOTPCode() {
        if (totpKey.isEmpty()) {
            return "";
        }

        String code = "";

        byte[] secretKey = this.totpKey.getBytes();

        @SuppressWarnings("deprecation")
        TOTPGenerator totp = new TOTPGenerator.Builder(secretKey)
            .withHOTPGenerator(builder -> {
                builder.withPasswordLength(6);
                builder.withAlgorithm(HMACAlgorithm.SHA1);
            })
            .withPeriod(Duration.ofSeconds(30))
            .build();

        try {
            code = totp.at(Instant.now());
            totpLabel.setText(code);
        } catch (IllegalStateException e) {
            e.printStackTrace(System.err);
        }

        return code;
    }


    @Override
    public void run() {
        SwingUtilities.invokeLater(
            new Runnable() {

                public void run() {
                    totpLabel.setText(getTOTPCode());
                }
            }
        );
    }
}
