import os

def combine_java_files(current_dir, output_file):
    """
    Combine tous les fichiers Java du répertoire courant et ses sous-répertoires
    dans un seul fichier, en préservant les déclarations de package.
    """
    # Ensemble pour stocker les packages déjà traités
    packages_seen = set()
    
    # Ouvrir le fichier de sortie
    with open(output_file, 'w', encoding='utf-8') as outfile:
        # Parcourir récursivement le répertoire
        for root, dirs, files in os.walk('.'):  # On commence à partir du répertoire courant ('.')
            for file in files:
                if file.endswith('.java'):
                    # Ignorer le fichier de sortie s'il existe déjà
                    if file == output_file:
                        continue
                        
                    file_path = os.path.join(root, file)
                    
                    with open(file_path, 'r', encoding='utf-8') as infile:
                        content = infile.read()
                        
                        # Ajouter un séparateur pour la lisibilité
                        outfile.write('\n' + '#' * 80 + '\n')
                        outfile.write(f'// Source: {file_path}\n')
                        outfile.write('#' * 80 + '\n\n')
                        
                        # Écrire le contenu du fichier
                        outfile.write(content)
                        outfile.write('\n\n')

# Utilisation du script
if __name__ == '__main__':
    output_file = "Generated classes.txt"
    combine_java_files('.', output_file)
    print(f"Les fichiers Java ont été combinés dans {output_file}")
    