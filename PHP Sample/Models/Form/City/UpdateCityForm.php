<?php
namespace app\Models\Form\City;

use app\Helper\View\CityViewHelper;
use app\Core\Domain\Model\City\City;
use app\Core\Domain\Repository\City\CityReadRepository;
use yii\base\Model;

class UpdateCityForm extends Model
{
    /**
     * @var string
     */
    public $id;

    /**
     * @var string
     */
    public $name;

    /**
     * @var int
     */
    public $country;

    /**
     * @var CityReadRepository
     */
    private $cityReadRepository;

    /**
     * @var int
     */
    public $extraCharge;

    /**
     * City constructor.
     * @param CityReadRepository $cityReadRepository
     * @param array $config
     */
    public function __construct(CityReadRepository $cityReadRepository, $config = [])
    {
        $this->cityReadRepository = $cityReadRepository;
        parent::__construct($config);
    }

    /**
     * @param City $city
     */
    public function defineAttributes(City $city)
    {
        $this->id = $city->getId();
        $this->name = $city->getName();
        $this->country = $city->getCountry();
        $this->extraCharge = $city->getExtraCharge();
    }

    /**
     * @inheritdoc
     */
    public function rules()
    {
        return [
            [['name'], 'trim'],
            [['name', 'country'], 'required'],
            ['extraCharge', 'double', 'max' => 100, 'min' => -100],
            ['name', 'validateName'],
            ['country', 'in', 'range' => array_keys(CityViewHelper::getCountriesArray())],
        ];
    }

    /**
     * @inheritdoc
     */
    public function attributeLabels()
    {
        return CityViewHelper::attributeLabels();
    }

    /**
     * @param $attribute
     */
    public function validateName($attribute)
    {
        $city = $this->cityReadRepository->findByNameAndCountry($this->name, $this->country);
        if ($city != null && $city->getId() != $this->id) {
            $this->addError($attribute,  'Такой город уже существует в этой стране.');
        }
    }

}