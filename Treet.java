import java.io.*;
import java.util.*;

/**
 * Created by VOSTRO on 8/12/2015.
 */

public class Treet implements Comparable<Treet>, Serializable {
    private String mAuthor;
    private String mDescription;
    private Date mCreationDate;

    public Treet(String Author, String Description, Date creationDate){
        this.mAuthor = Author;
        this.mDescription = Description;
        this.mCreationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Treet{" +
                "mAuthor='" + mAuthor + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mCreationDate=" + mCreationDate +
                '}';
    }

    @Override
    public int compareTo(Treet other) {
        if (equals(other)) {
            return 0;
        }
        int dateCmp = mCreationDate.compareTo(other.mCreationDate);
        if (dateCmp==0) {
            return mDescription.compareTo(other.mDescription);
        }
        return dateCmp;
    }

    public String getAuthor(){
        return mAuthor;
    }

    public String getDescription(){
        return mDescription;
    }

    public Date getDate(){
        return mCreationDate;
    }

    public List<String> getWords(){
        String[] words = mDescription.toLowerCase().split("[^\\w#@']+");
        return Arrays.asList(words);
    }

    private List<String> getStringPrefixedWith(String prefix) {
        List<String> results = new ArrayList<>();
        for (String eachWord : getWords()) {
            if (eachWord.startsWith(prefix)) {
                results.add(eachWord);
            }
        }
        return results;
    }

    public List<String> getHashTags() {
        return getStringPrefixedWith("#");
    }

    public List<String> getMentions() {
        return getStringPrefixedWith("@");
    }

    public static void save(Treet[] treets) throws IOException {
        try (
                FileOutputStream fileOutputStream = new FileOutputStream("treets.ser");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        ) {
            objectOutputStream.writeObject(treets);
        } catch (IOException ioe) {
            System.out.println("Problem Saving Treets.");
            ioe.printStackTrace();
        }
    }

    public static Treet[] load() {
        Treet[] treets = new Treet[0];
        try(
                FileInputStream fileInputStream = new FileInputStream("treets.ser");
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                ){
            treets = (Treet[]) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading treets");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading file.");
            e.printStackTrace();
        }
        return treets;
    }

    public static void main(String[] args) throws IOException {
        Treet treet1 = new Treet("Andy!", "Hey there, it's me #andy . Found me on twitter @andynrkri. Cheers!", new Date(1421849732000L));
        Treet treet2 = new Treet("Prashant", "Hey there, I'm not coming to #FuckingNIET today.find me on @prasshant.",new Date(1421849732000L));
        Treet[] treets = {treet1, treet2};
        Arrays.sort(treets);
        for (Treet exampleTreet : treets) {
            System.out.println(exampleTreet);
        }
        save(treets);
        Treet[] reloadedtreets = load();
        System.out.printf("There are %d Treets. %n", reloadedtreets.length);

        Set<String> allHashTags = new HashSet<>();
        Set<String> allMentions = new HashSet<>();
        for (Treet treet : treets) {
            allHashTags.addAll(treet.getHashTags());
            allMentions.addAll(treet.getMentions());
        }
        System.out.printf("Hash tags: %s %n", allHashTags);
        System.out.printf("Mentions: %s %n", allMentions);

        Map<String, Integer> hashTagCounts = new HashMap<>();
        for (Treet treet : treets) {
            for (String hashtag : treet.getHashTags()) {
                Integer count = hashTagCounts.get(hashtag);
                if (count == null) {
                    count =0;
                }
                count++;
                hashTagCounts.put(hashtag, count);
            }
        }
        System.out.printf("Hashtag counts: %s %n", hashTagCounts);

        Map<String, List<Treet>> treetsByAuthor = new HashMap<>();
        for (Treet treet : treets) {
            List<Treet> authoredTreets = treetsByAuthor.get(treet.getAuthor());
            if (authoredTreets == null) {
                authoredTreets = new ArrayList<Treet>();
                treetsByAuthor.put(treet.getAuthor(), authoredTreets);
            }
            authoredTreets.add(treet);
        }
        System.out.printf("Treets by author: %s %n", treetsByAuthor);
        System.out.printf("Treets by andy: %s %n", treetsByAuthor.get("andy"));

        int i = 0;
        int reloadedtreetsLength = reloadedtreets.length;
        while (i < reloadedtreetsLength) {
            Treet originalTreet = reloadedtreets[i];
            System.out.println("Hashtags: ");
            for (String hashtag : originalTreet.getHashTags()) {
                System.out.println(hashtag);
            }
            System.out.println("Mentions: ");
            for (String mentions : originalTreet.getMentions()) {
                System.out.println(mentions);
            }
            i++;
        }
    }
        /*
        *System.arraycopy();
        * Arrays.copy();
        * find out more about them!!
        */
}
