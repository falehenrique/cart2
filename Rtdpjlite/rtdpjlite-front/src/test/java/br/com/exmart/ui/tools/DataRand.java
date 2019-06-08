package br.com.exmart.ui.tools;

/**
 * Created by Heryk on 05/07/2018.
 */
public class DataRand {

    String[] nmNome = {
            "Silvia",
            "Paulo",
            "Mauro",
            "Bianca",
            "Rosana",
            "Silveira",
            "João",
            "Pedro",
            "Gonçalves"
    };

    String[] nmSobrenome = {
            "Silva",
            "Santos",
            "Azevedo",
            "Bragança",
            "Gonçalves",
            "Castro"
    };

    public DataRand() {
    }

    int randM(int min, int max)
    {
        int range = Math.abs(max - min) + 1;
        return (int)(Math.random() * range) + (min <= max ? min : max);
    }



    public String getNome() {
        return nmNome[randM(0,nmNome.length-1)] +" "+ nmSobrenome[randM(0,nmSobrenome.length-1)];
    }

    public String getTelefone() {
        String tel = "";
        for (int i = 1; i <= 9; i++) {
            if (i == 6) {
                tel = tel + "-";
            }
            tel = tel + Integer.toString(randM(0, 9));
        }
        return tel;
    }

    public String getIdentidade() {
        String tel = "";
        for (int i = 1; i <= 11; i++) {
            tel = tel + Integer.toString(randM(0, 9));
        }
        return tel;
    }

    public String getEmail() {
        return "eu@eu.com.br";
    }

    public String getLoremIpsum() {
        return "adnas daskdlasdjk asld iasd uasodu iasjdnasdajgsbd kasjkda ksdjha skd,asnd,amshd kasjhabsmdnbsdasdgak sasjdbasndbasd";
    }

    public String getEndereco() {
        return "Rua Glória da Silva";
    }

    public String getDocumento() {
        return "11122233396";
    }

    public String getValor() {
        return "150000";
    }


}
