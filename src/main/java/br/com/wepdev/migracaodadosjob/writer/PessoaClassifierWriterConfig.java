package br.com.wepdev.migracaodadosjob.writer;

import br.com.wepdev.migracaodadosjob.dominio.Pessoa;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.builder.ClassifierCompositeItemWriterBuilder;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PessoaClassifierWriterConfig {

    @Bean
    public ClassifierCompositeItemWriter<Pessoa> pesssoaClassifierWriter(
            JdbcBatchItemWriter<Pessoa> bancoPessoaWriter,
            FlatFileItemWriter<Pessoa> arquivoPessoasInvalidasWriter){
        
        return new ClassifierCompositeItemWriterBuilder<Pessoa>()
                .classifier(classifier(bancoPessoaWriter, arquivoPessoasInvalidasWriter)) // Verifica qual escritor sera utilizado
                .build();
    }

    private Classifier<Pessoa, ItemWriter<? super Pessoa>> classifier(JdbcBatchItemWriter<Pessoa> bancoPessoaWriter, FlatFileItemWriter<Pessoa> arquivoPessoasInvalidasWriter) {

        return new Classifier<Pessoa, ItemWriter<? super Pessoa>>() {
            @Override
            public ItemWriter<? super Pessoa> classify(Pessoa pessoa) {
                if(pessoa.isValida()) // Verifica se as propriedades obrigatorias de pessoas foram informadas, foi implementado dentro de pessoa ja ela mesma conhece suas propriedades
                    return bancoPessoaWriter; // se a pessoa for valida escreve no banco
                else
                    return arquivoPessoasInvalidasWriter; // se a pessoa nao for valida escreve no arquivo de pessoas invalidas
            }
        };
    }
}
