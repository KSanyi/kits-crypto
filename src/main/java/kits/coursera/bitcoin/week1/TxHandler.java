package kits.coursera.bitcoin.week1;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TxHandler {

    private UTXOPool utxoPool;
    
    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
        this.utxoPool = utxoPool;
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
     
        // (1)
        if(!tx.getInputs().stream()
                .allMatch(input -> utxoPool.contains(new UTXO(input.prevTxHash, input.outputIndex)))) {
            return false;
        }
        
        // (2)
        if(!areInputSignaturesValid(tx)) {
            return false;
        }
        
        // (3)
        if(!noDoubleSpending(tx)) {
            return false;
        }
        
        // (4)
        if(!tx.getOutputs().stream()
                .allMatch(output -> output.value >= 0)) {
            return false;
        }
        
        // (5)
        if(!sumInputIsNoLessThenSumOutput(tx)) {
            return false;
        }
        
        return true;
    }
    
    private boolean areInputSignaturesValid(Transaction tx) {
        
        for(int index=0;index<tx.getInputs().size();index++) {
            Transaction.Input input = tx.getInput(index);
            byte[] dataToSign = tx.getRawDataToSign(index);
            PublicKey publicKey = utxoPool.getTxOutput(new UTXO(input.prevTxHash, input.outputIndex)).address;
            if(!Crypto.verifySignature(publicKey, dataToSign, input.signature)){
                return false;
            }
        }
        
        return true;
    }
    
    private boolean noDoubleSpending(Transaction tx) {
        
        Set<UTXO> referencedUTXOs = new HashSet<>();
        
        for(Transaction.Input input : tx.getInputs()) {
            UTXO utxO = new UTXO(input.prevTxHash, input.outputIndex);
            if(referencedUTXOs.contains(utxO)) {
                return false;
            }
            referencedUTXOs.add(utxO);
        }
        
        return true;
    }
    
    private boolean sumInputIsNoLessThenSumOutput(Transaction tx) {
        
        double sumInput = tx.getInputs().stream()
            .mapToDouble(input -> utxoPool.getTxOutput(new UTXO(input.prevTxHash, input.outputIndex)).value)
            .sum();
        
        double sumOutput = tx.getOutputs().stream()
                .mapToDouble(output -> output.value)
                .sum();
        
        return sumInput >= sumOutput;
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        
        List<Transaction> validTransactions = Stream.of(possibleTxs)
                .filter(this::isValidTx)
                .collect(Collectors.toList());
        
        List<Transaction> best = new ArrayList<>();
        for(int i=0;i<10;i++) {
            List<Transaction> succesfullTransactions = test(validTransactions);
            if(succesfullTransactions.size() > best.size()) {
                best = succesfullTransactions;
            }
        }
        
        execute(best);
        
        return best.toArray(new Transaction[0]);
    }
    
    private List<Transaction> test(List<Transaction> validTransactions) {
        
        UTXOPool originalUtxoPool = new UTXOPool(utxoPool);
        
        List<Transaction> succesfullTransactions = new ArrayList<>();
        
        Collections.shuffle(validTransactions);
        
        for(Transaction transaction : validTransactions) {
            if(isValidTx(transaction)) {
                for(Transaction.Input input : transaction.getInputs()) {
                    UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
                    utxoPool.removeUTXO(utxo);
                }
                for(int index=0;index<transaction.getOutputs().size();index++) {
                    UTXO utxo = new UTXO(transaction.getHash(), index);
                    utxoPool.addUTXO(utxo, transaction.getOutput(index));
                }
                succesfullTransactions.add(transaction);
            }
        }
        
        utxoPool = originalUtxoPool;
        
        return succesfullTransactions;
    }
    
    private void execute(List<Transaction> succesfullTransactions) {
        for(Transaction transaction : succesfullTransactions) {
            for(Transaction.Input input : transaction.getInputs()) {
                UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
                utxoPool.removeUTXO(utxo);
            }
            for(int index=0;index<transaction.getOutputs().size();index++) {
                UTXO utxo = new UTXO(transaction.getHash(), index);
                utxoPool.addUTXO(utxo, transaction.getOutput(index));
            }
        }
    }

}
