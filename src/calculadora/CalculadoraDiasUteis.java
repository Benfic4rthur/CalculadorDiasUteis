package calculadora;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;

public class CalculadoraDiasUteis extends JDialog {
	
	
	private static final long serialVersionUID = 1L;
	
	private JPanel jpanel = new JPanel(new GridBagLayout());
	private JLabel descricaoHora = new JLabel("Data atual:"); 
	private JLabel dataEscolhidaJLabel = new JLabel("Digite a data:");
	private JLabel dataCalculadaJLabel = new JLabel("Resultado:");
	private JTextField mostraTempo = new JTextField();
	private JTextField dataEscolhidaField = new JTextField();
	private JTextField mostraDataCalculada = new JTextField();
	private JButton jButton = new JButton("Calcular");
	
	private Runnable thread1 = new Runnable() {
		@Override
		public void run() {
			while (true) {
				mostraTempo.setText(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
				try { 
					Thread.sleep(1000); 
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};
	private boolean isFeriado(LocalDate data) {
	    // lista com todas as datas dos feriados do ano
	    List<LocalDate> feriados = Arrays.asList(
	        LocalDate.of(data.getYear(), 1, 1),   // Ano Novo
	        LocalDate.of(data.getYear(), 4, 7),   // paixão de cristo
	        LocalDate.of(data.getYear(), 4, 21),  // Tiradentes
	        LocalDate.of(data.getYear(), 5, 1),   // Dia do Trabalho
	        LocalDate.of(data.getYear(), 9, 7),   // Independência do Brasil
	        LocalDate.of(data.getYear(), 10, 12), // Nossa Senhora Aparecida
	        LocalDate.of(data.getYear(), 11, 2),  // Finados
	        LocalDate.of(data.getYear(), 11, 15), // Proclamação da República
	        LocalDate.of(data.getYear(), 12, 25)  // Natal
	    );
	    // verifica se a data está na lista de feriados
	    return feriados.contains(data);
	}

	private ActionListener acaoBotao = new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
	        String dataAtualString = mostraTempo.getText();
	        LocalDate dataAtual = LocalDate.parse(dataAtualString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	        String dataString = formatarData(dataEscolhidaField.getText().trim());
	        LocalDate dataEscolhida = LocalDate.parse(dataString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	        int diasUteis = 0;
	        while (dataAtual.isBefore(dataEscolhida)) {
	            if (dataAtual.getDayOfWeek() != DayOfWeek.SATURDAY && dataAtual.getDayOfWeek() != DayOfWeek.SUNDAY && !isFeriado(dataAtual)) {
	                diasUteis++;
	            }
	            dataAtual = dataAtual.plusDays(1);
	        }

	        String resultado = String.format("%d dias úteis", diasUteis);
	        mostraDataCalculada.setText(resultado);
	    }
	};
	/**
	 * Formata a data no padrão "dd/MM/yyyy", adicionando as barras se necessário.
	 * @param data a data a ser formatada
	 * @return a data formatada
	 */
	private String formatarData(String data) {
	    String dataFormatada = data.replaceAll("[^0-9]", ""); // remove todos os caracteres que não são dígitos
	    if (dataFormatada.length() != 8) { // se a data não tem 8 dígitos, não pode ser formatada
	        return data;
	    }
	    return dataFormatada.substring(0, 2) + "/" + dataFormatada.substring(2, 4) + "/" + dataFormatada.substring(4);
	}
	private FocusAdapter focusAdapter = new FocusAdapter() {
	    @Override
	    public void focusLost(FocusEvent e) {
	        dataEscolhidaField.setText(formatarData(dataEscolhidaField.getText()));
	    }
	};
	
	public void init() {
		jpanel.add(descricaoHora, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		jpanel.add(mostraTempo, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		jpanel.add(dataEscolhidaJLabel, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		jpanel.add(dataEscolhidaField, new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		jpanel.add(jButton, new GridBagConstraints(0, 2, 2, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		jpanel.add(dataCalculadaJLabel, new GridBagConstraints(0, 3, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		jpanel.add(mostraDataCalculada, new GridBagConstraints(1, 3, 1, 1,0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		jButton.addActionListener(acaoBotao);
		
		setContentPane(jpanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 300);
		setVisible(true);

		Thread t1 = new Thread(thread1);
		t1.start();
		// Adicione o código abaixo para corrigir a entrada de data do usuário
	    dataEscolhidaField.addFocusListener(new FocusAdapter() {
	        @Override
	        public void focusLost(FocusEvent e) {
	            String data = dataEscolhidaField.getText();
	            if (data.matches("\\d{8}")) {
	                data = data.replaceAll("(\\d{2})(\\d{2})(\\d{4})", "$1/$2/$3");
	                dataEscolhidaField.setText(data);
	            }
	        }
	    });
	}
	private Thread thread1Time;
	public CalculadoraDiasUteis() {
	    setTitle("calculadora dias uteis");
	    setSize(new Dimension(240, 300));
	    setLocationRelativeTo(null);
	    setResizable(false);
	    ImageIcon icon = new ImageIcon("C:\\workspace-java\\CalculadoraDiasUteis\\src\\calculadora\\icone.png");
	    setIconImage(icon.getImage());
	    GridBagConstraints gridBagConstraints = new GridBagConstraints();
	    gridBagConstraints.gridx = 0;
	    gridBagConstraints.gridy = 0;
	    gridBagConstraints.gridwidth = 2;
	    gridBagConstraints.insets = new Insets(5, 10, 5, 5);
	    gridBagConstraints.anchor = GridBagConstraints.WEST;
	    descricaoHora.setPreferredSize(new Dimension(200, 25));
	    jpanel.add(descricaoHora, gridBagConstraints);
	    mostraTempo.setPreferredSize(new Dimension(200, 25));
	    gridBagConstraints.gridy++;
	    mostraTempo.setText(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
	    mostraTempo.setEditable(false);
	    jpanel.add(mostraTempo, gridBagConstraints);
	    gridBagConstraints.gridy++;
	    dataEscolhidaJLabel.setPreferredSize(new Dimension(200, 25));
	    jpanel.add(dataEscolhidaJLabel, gridBagConstraints);
	    dataEscolhidaField.setPreferredSize(new Dimension(200, 25));
	    dataEscolhidaField.addFocusListener(focusAdapter);
	    gridBagConstraints.gridy++;
	    jpanel.add(dataEscolhidaField, gridBagConstraints);
	    gridBagConstraints.gridy++;
	    dataCalculadaJLabel.setPreferredSize(new Dimension(200, 25));
	    jpanel.add(dataCalculadaJLabel, gridBagConstraints);
	    mostraDataCalculada.setPreferredSize(new Dimension(200, 25));
	    gridBagConstraints.gridy++;
	    mostraDataCalculada.setEditable(false);
	    jpanel.add(mostraDataCalculada, gridBagConstraints);
	    gridBagConstraints.gridy++;
	    gridBagConstraints.gridwidth = 1;
	    jButton.addActionListener(acaoBotao);
	    jButton.setPreferredSize(new Dimension(92, 25));
	    gridBagConstraints.gridy++;
	    jpanel.add(jButton, gridBagConstraints);
	    jButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            thread1Time = new Thread(thread1);
	            thread1Time.start();
	            try {
	                if (dataEscolhidaField.getText().trim().isEmpty()) {
	                    JOptionPane.showMessageDialog(jpanel, "Data não informada", "Erro", JOptionPane.ERROR_MESSAGE);
	                    return;
	                }
	                String datasemformatacao = dataEscolhidaField.getText();
	                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	                dateFormat.setLenient(false);
	                Date dataEscolhida = dateFormat.parse(datasemformatacao);
	                String dataFormatada = dateFormat.format(dataEscolhida);
	                dataEscolhidaField.setText(dataFormatada);
	            } catch (ParseException ex) {
	                JOptionPane.showMessageDialog(jpanel, "Data inválida", "Erro", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    });
	    add(jpanel, BorderLayout.WEST);
	    setVisible(true);
	}
}