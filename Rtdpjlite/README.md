#RTDPJ

##Dependências

- [Java 8](https://openjdk.java.net)
- [Maven](https://maven.apache.org)
- [PostgreSQL](https://www.postgresql.org)
- [BouncyCastle](https://www.bouncycastle.org)
- [PDFA](https://www.pdftron.com/documentation/cli/guides/pdfa-manager/overview)
- [PAGEMASTER](https://www.pdftron.com/documentation/cli/guides/pdf-pagemaster/overview)
- [Ghostscript 8.7 ou superior](https://www.ghostscript.com)


###Configuração do BouncyCastle - Security Provider

Editar o arquivo **$JAVA_HOME/jre/lib/security/java.security** e acrescentar a linha abaixo.
  
`security.provider.N=org.bouncycastle.jce.provider.BouncyCastleProvider`

*Substituir o **N** pelo próximo número da sequência de configurações disponíveis.

Efetuar o download do [jar do BouncyCastle](https://www.bouncycastle.org/download/bcprov-jdk15on-159.jar) e mover para o diretório: **$JAVA_HOME/jre/lib/ext**.


###Configuração do PDFA e PAGEMASTER

Efetuar o download dos utilitários [PDFA](https://www.pdftron.com/documentation/cli/guides/pdfa-manager/download) e [PAGEMASTER](https://www.pdftron.com/documentation/cli/guides/pdf-pagemaster/download).

Adicionar a pasta raiz de cada utilitário no **PATH**.


###Configuração do GhostScript

Efetuar o download do utilitário [GhostScript](https://www.ghostscript.com/download/gsdnld.html).

Adicionar a pasta raiz do utilitário no **PATH**.


###Variáveis de Ambiente

```
export RTDPJ_DATABASE_IP=localhost
export RTDPJ_DATABASE_PORT=5432
export RTDPJ_DATABASE=rtdpjlite_test
export RTDPJ_DATABASE_USERNAME=exmart
export RTDPJ_DATABASE_PASSWORD=
export RTDPJ_URL_FINANCEIRO='http://homologa-rtdpj.2osasco.com.br/cartoriointeligente'
export RTDPJ_URL_FONETICA='http://homologa-rtdpj.2osasco.com.br/fonetica/'
export RTDPJ_URL_INDISPONIBILIDADE='http://homologa-rtdpj.2osasco.com.br/indisponibilidade'
export RTDPJ_URL_SELO_ELETRONICO='http://homologa-rtdpj.2osasco.com.br/seloeletronico'
```

##Rodando local

###Pela primeira vez

Na raiz do projeto executar o comando:

`mvn --projects rtdpjlite-data flyway:clean flyway:migrate flyway:info -DskipTests clean install`

E logo em seguida, dentro da pasta *rtdpjlite-front*, executar o comando: 

`mvn spring-boot:run`


###No dia a dia com migration

Na raiz do projeto executar o comando:

`mvn --projects rtdpjlite-data flyway:migrate flyway:info -DskipTests clean install`

E logo em seguida, dentro da pasta *rtdpjlite-front*, executar o comando: 

`mvn spring-boot:run`


###No dia a dia sem migration

`mvn --projects rtdpjlite-data install -DskipTests`

E logo em seguida, dentro da pasta *rtdpjlite-front*, executar o comando: 

`mvn spring-boot:run`


##Testes

###Rodar todos

`mvn test`

###Rodar classe em específico

`mvn --projects rtdpjlite-data -Dtest=CustaRepositoryTest test`
