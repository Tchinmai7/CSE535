from django.db import models


# Create your models here.
class User(models.Model):
    UserName = models.CharField(max_length=100)
    ClassifierName = models.CharField(max_length=100)
    UserSignalFile = models.FileField(blank=False, null=False)

    def __str__(self):
        return "{0}, {1}, {2}".format(self.ClassifierName, self.UserName, self.UserSignalFile.name)
